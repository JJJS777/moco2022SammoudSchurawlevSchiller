package com.example.tierdex.fragments.camera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.hardware.display.DisplayManager
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.tierdex.R
import com.example.tierdex.databinding.CameraFragmentBinding
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.lang.Math.*
import java.text.SimpleDateFormat
import java.util.*
import androidx.camera.core.ImageCapture.Metadata
import androidx.core.net.toFile
import androidx.core.view.setPadding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.tierdex.model.LuminosityAnalyzer
import com.example.tierdex.model.Permissions
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/** Helper type alias used for analysis use case callbacks */
typealias LumaListener = (luma: Double) -> Unit

class CameraFragment : Fragment() {

    private var _binding: CameraFragmentBinding? = null
    private val binding get() = _binding!!

    // permission
    private lateinit var managePermissions: Permissions

    // camera
    private lateinit var outputDirectory: File
    private var imageCapture: ImageCapture? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private var preview: Preview? = null
    private var camera: Camera? = null
    private var imageAnalyzer : ImageAnalysis? = null


    /** Blocking camera operations are performed using this executor */
    private lateinit var cameraExecutor: ExecutorService

    override fun onResume() {
        super.onResume()
        // Make sure that all permissions are still present
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
        // Shut down our background executor
        cameraExecutor.shutdown()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CameraFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        managePermissions = Permissions(requireActivity(), REQUIRED_PERMISSIONS, PERMISSION_CODE)
        managePermissions.checkPermissions()

        startCamera()

        cameraExecutor = Executors.newSingleThreadExecutor()

        // Determine the output directory
        outputDirectory = getOutputDirectory(requireContext())

        // Wait for the views to be properly laid out
        binding.viewFinder.post {

            // Build UI controls
            updateCameraUi()
        }
    }



    private fun updateCameraUi(){
        binding.btnTakePicture.setOnClickListener { takephoto() }
    }

    /** Declare and bind preview, capture and analysis use cases */
    private fun bindCameraUseCases() {

        val metric = context?.resources?.displayMetrics
        val screenAspectRatio = metric?.let { aspectRatio(it.widthPixels,metric.heightPixels) }
        Log.d(TAG, "Preview aspect ratio: $screenAspectRatio")

        val rotation = binding.viewFinder.display.rotation

        // CameraProvider
        val cameraProvider = cameraProvider
            ?: throw IllegalStateException("Camera initialization failed.")

        // CameraSelector
        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        // Preview
        preview = screenAspectRatio?.let {
            Preview.Builder()
                // We request aspect ratio but no resolution
                .setTargetAspectRatio(it)
                // Set initial target rotation
                .setTargetRotation(rotation)
                .build()
        }

        // ImageCapture
        imageCapture = screenAspectRatio?.let {
            ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                // We request aspect ratio but no resolution to match preview config, but letting
                // CameraX optimize for whatever specific resolution best fits our use cases
                .setTargetAspectRatio(it)
                // Set initial target rotation, we will have to call this again if rotation changes
                // during the lifecycle of this use case
                .setTargetRotation(rotation)
                .build()
        }
        // ImageAnalysis
        imageAnalyzer = screenAspectRatio?.let {
            ImageAnalysis.Builder()
                // We request aspect ratio but no resolution
                .setTargetAspectRatio(it)
                // Set initial target rotation, we will have to call this again if rotation changes
                // during the lifecycle of this use case
                .setTargetRotation(rotation)
                .build()
                // The analyzer can then be assigned to the instance
                .also {
                    it.setAnalyzer(cameraExecutor, LuminosityAnalyzer { luma ->
                        // Values returned from our analyzer are passed to the attached listener
                        // We log image analysis results here - you should do something useful
                        // instead!
                        Log.d("Analyzer", "Average luminosity: $luma")
                    })
                }
        }


        // Must unbind the use-cases before rebinding them
        cameraProvider.unbindAll()

        try {
            // A variable number of use-cases can be passed here -
            // camera provides access to CameraControl & CameraInfo
            camera = cameraProvider.bindToLifecycle(
                this, cameraSelector, preview, imageCapture, imageAnalyzer)

            // Attach the viewfinder's surface provider to preview use case
            preview?.setSurfaceProvider(binding.viewFinder.surfaceProvider)
        } catch (exc: Exception) {
            Log.e(TAG, "Use case binding failed", exc)
        }
    }

    private fun startCamera(){
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            // CameraProvider
            cameraProvider = cameraProviderFuture.get()

            // Build and bind the camera use cases
            bindCameraUseCases()

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun takephoto(){
        imageCapture?.let { imageCapture ->
            // Create output file to hold the image
            val photoFile = createFile(outputDirectory, FILENAME, PHOTO_EXTENSION)
            // Setup image capture metadata
            val metadata = Metadata()
            // Create output options object which contains file + metadata
            val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile)
                .setMetadata(metadata)
                .build()

            // Setup image capture listener which is triggered after photo has been taken
            imageCapture.takePicture(
                outputOptions, cameraExecutor, object : ImageCapture.OnImageSavedCallback {
                    override fun onError(exc: ImageCaptureException) {
                        Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                    }

                    override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                        val savedUri = output.savedUri ?: Uri.fromFile(photoFile)
                        Toast.makeText(requireContext(), "msg", Toast.LENGTH_SHORT).show()

                        // We can only change the foreground Drawable using API level 23+ API
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            // Update the gallery thumbnail with latest picture taken
                            setGalleryThumbnail(savedUri)
                        }

                        // If the folder selected is an external media directory, this is
                        // unnecessary but otherwise other apps will not be able to access our
                        // images unless we scan them using [MediaScannerConnection]
                        val mimeType = MimeTypeMap.getSingleton()
                            .getMimeTypeFromExtension(savedUri.toFile().extension)
                        MediaScannerConnection.scanFile(
                            context,
                            arrayOf(savedUri.toFile().absolutePath),
                            arrayOf(mimeType)
                        ) { _, uri ->
                            Log.d(TAG, "Image capture scanned into media store: $uri")
                        }

                    }
                })

            // We can only change the foreground Drawable using API level 23+ API
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                // Display flash animation to indicate that photo was captured
                binding.root.postDelayed({
                    binding.root.foreground = ColorDrawable(Color.WHITE)
                    binding.root.postDelayed(
                        { binding.root.foreground = null }, 50L)
                }, 100L)
            }
        }
    }


    private fun setGalleryThumbnail(uri: Uri) {
        // Run the operations in the view's thread
        binding.btnGallery.let { photoViewButton ->
            photoViewButton.post {
                // Remove thumbnail padding
                photoViewButton.setPadding(resources.getDimension(R.dimen.stroke_small).toInt())

                // Load thumbnail into circular button using Glide
                Glide.with(photoViewButton)
                    .load(uri)
                    .apply(RequestOptions.circleCropTransform())
                    .into(photoViewButton)
            }
        }
    }


    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }

    companion object{
        private const val TAG = "CameraXBasic"
        private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val PHOTO_EXTENSION = ".jpg"
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0

        private val PERMISSION_CODE = 100
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()

        /** Helper function used to create a timestamped file */
        private fun createFile(baseFolder: File, format: String, extension: String) =
            File(baseFolder, SimpleDateFormat(format, Locale.US)
                .format(System.currentTimeMillis()) + extension)

        /** output */
        fun getOutputDirectory(context: Context): File {
            val appContext = context.applicationContext
            val mediaDir = context.getExternalFilesDir(null).let {
                File(it, appContext.resources.getString(R.string.app_name)).apply { mkdirs() }
            }
            return if (mediaDir.exists())
                mediaDir else appContext.filesDir
        }
    }
}