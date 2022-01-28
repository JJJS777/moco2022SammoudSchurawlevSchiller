package com.example.tierdex.fragments.camera


import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.net.Uri
import android.os.*
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.concurrent.Executors
import androidx.camera.core.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

import androidx.camera.lifecycle.ProcessCameraProvider
import java.util.concurrent.ExecutorService
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.view.View
import androidx.camera.core.ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import coil.fetch.VideoFrameUriFetcher
import coil.load
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.tierdex.databinding.CameraFragmentBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import com.example.tierdex.R
import com.example.tierdex.model.LuminosityAnalyzer

class CameraFragment : BaseFragment<CameraFragmentBinding>(R.layout.camera_fragment){

    override val binding: CameraFragmentBinding by lazy { CameraFragmentBinding.inflate(layoutInflater) }

    private lateinit var cameraExecutor: ExecutorService
    private var imageCapture: ImageCapture? = null
    private var lensFacing = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageAnalyzer: ImageAnalysis? = null



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        binding.btnTakePicture.setOnClickListener { takePhoto() }
        binding.btnGallery.setOnClickListener { openPreview() }
        cameraExecutor = Executors.newSingleThreadExecutor()

    }

    private fun takePhoto() = lifecycleScope.launch(Dispatchers.Main) {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: throw IllegalStateException("Camera initialization failed.")

        // Setup image capture metadata

        val metadata = ImageCapture.Metadata().apply {
            // Mirror image when using the front camera
            isReversedHorizontal = lensFacing == CameraSelector.DEFAULT_FRONT_CAMERA
        }

        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        // Options fot the output image file
        val outputOptions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, System.currentTimeMillis())
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, outputDirectory)
            }

            val contentResolver = requireContext().contentResolver

            // Create the output uri
            val contentUri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)

            ImageCapture.OutputFileOptions.Builder(contentResolver, contentUri, contentValues)
        } else {
            File(outputDirectory).mkdirs()
            val file = File(outputDirectory, "${System.currentTimeMillis()}.jpg")

            ImageCapture.OutputFileOptions.Builder(file)
        }.setMetadata(metadata).build()
        // Set up image capture listener, which is triggered after photo has
        // been taken
        Log.d(TAG, "over image")
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults){
                    // This function is called if capture is successfully completed
                    output.savedUri
                        ?.let { uri ->
                            setGalleryThumbnail(uri)
                            Log.d(TAG, "Photo saved in $uri")
                        }
                        ?: setLastPictureThumbnail()
                }
            }
        )
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // The display information
            val metrics = DisplayMetrics().also { binding.viewFinder.display.getRealMetrics(it) }
            // The ratio for the output image and preview
            val aspectRatio = aspectRatio(metrics.widthPixels, metrics.heightPixels)
            // The display rotation
            val rotation = binding.viewFinder.display.rotation

            // The Configuration of camera preview
            val preview = Preview.Builder()
                .setTargetAspectRatio(aspectRatio)
                .setTargetRotation(rotation)
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            // The Configuration of image capture
            imageCapture = ImageCapture.Builder()
                .setCaptureMode(CAPTURE_MODE_MAXIMIZE_QUALITY) // setting to have pictures with highest quality possible (may be slow)
                .setTargetAspectRatio(aspectRatio) // set the capture aspect ratio
                .setTargetRotation(rotation) // set the capture rotation
                .build()

            // The Configuration of image analyzing
            imageAnalyzer = ImageAnalysis.Builder()
                .setTargetAspectRatio(aspectRatio) // set the analyzer aspect ratio
                .setTargetRotation(rotation) // set the analyzer rotation
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST) // in our analysis, we care about the latest image
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, LuminosityAnalyzer { luma ->
                    Log.d(TAG, "Average luminosity: $luma")
                })
                }

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()
                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, lensFacing, preview)
            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }
        },ContextCompat.getMainExecutor(requireContext()))
    }

    /**
     * Navigate to PreviewFragment
     * */
    private fun openPreview() {
        if (getMedia().isEmpty()) return
        view?.let { Navigation.findNavController(it).navigate(R.id.NO_DEBUG) }
    }

    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }

    private fun setLastPictureThumbnail() = binding.btnGallery.post {
        getMedia().firstOrNull() // check if there are any photos or videos in the app directory
            ?.let { setGalleryThumbnail(it.uri) } // preview the last one
            ?: binding.btnGallery.setImageResource(R.drawable.ic_no_picture) // or the default placeholder
    }

    private fun setGalleryThumbnail(savedUri: Uri?) = binding.btnGallery.load(savedUri) {
        placeholder(R.drawable.ic_no_picture)
        transformations(CircleCropTransformation())
        listener(object : ImageRequest.Listener {
            override fun onError(request: ImageRequest, throwable: Throwable) {
                super.onError(request, throwable)
                binding.btnGallery.load(savedUri) {
                    placeholder(R.drawable.ic_no_picture)
                    transformations(CircleCropTransformation())
                    fetcher(VideoFrameUriFetcher(requireContext()))
                }
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(requireContext(),
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Check for the permissions
     */
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val TAG = "CameraXApp"

        private const val RATIO_4_3_VALUE = 4.0 / 3.0 // aspect ratio 4x3
        private const val RATIO_16_9_VALUE = 16.0 / 9.0 // aspect ratio 16x9

        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }

    override fun onBackPressed() = requireActivity().finish()

}

