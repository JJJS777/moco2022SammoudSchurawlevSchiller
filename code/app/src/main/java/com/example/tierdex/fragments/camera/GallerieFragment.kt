package com.example.tierdex.fragments.camera

import android.Manifest
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.navigation.Navigation
import com.example.tierdex.R
import com.example.tierdex.adapter.GridImageAdapter
import java.io.File
import com.example.tierdex.databinding.FragmentGallerieBinding
import com.example.tierdex.fragments.AddDiscoveryFragment
import com.example.tierdex.model.Permissions
import com.squareup.picasso.Picasso
import java.lang.Exception

val EXTENSION_WHITELIST = arrayOf("JPG")

class GallerieFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor>{

    private var _binding : FragmentGallerieBinding? = null
    private val binding get () = _binding!!

    // permission
    private lateinit var managePermissions: Permissions


    private var gridWidth :Int = 0
    private var imageWidth : Int = 0

    private lateinit var file: File
    private lateinit var uri : Uri


    private val IMAGE_LOADER_ID = 1
    private val listOfAllImages = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LoaderManager.getInstance(this).initLoader(IMAGE_LOADER_ID,null,this)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentGallerieBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        managePermissions = Permissions(requireActivity(), PERMISSIONS, PERMISSION_CODE)
        managePermissions.checkPermissions()


        binding.nextButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("photo",uri.toString())
            val fragment = AddDiscoveryFragment()
            fragment.arguments = bundle

            Navigation.findNavController(view).navigate(R.id.action_galleryImageView_to_addDiscoveryFragment, bundle)
        }

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }


    private fun setupGridView(allImages : ArrayList<String>){

        //set the grid column width
        gridWidth = resources.displayMetrics.widthPixels
        imageWidth = gridWidth / NUM_GRID_COLUMNS

        binding.gridView.setColumnWidth(imageWidth)

        // grid Adapter
        val adapter = GridImageAdapter(requireActivity(),R.layout.image_view,allImages)

        binding.gridView.adapter = adapter

        //set the first image to be displayed when the activity fragment view is inflated
        file = File(allImages[0])
        uri = Uri.fromFile(file)
        setImage(uri, binding.galleryImageView);

        binding.gridView.setOnItemClickListener { parent, view, position, id ->
            Log.d("GRIDVIEW", "onItemClick: selected an image: " + allImages.get(position))
            file = File(allImages[position])
            uri = Uri.fromFile(file)
            setImage(uri, binding.galleryImageView)
        }

    }

    private fun setImage(imgUrl : Uri, image : ImageView){
        val galleryImageWidth = resources.displayMetrics.widthPixels
        val galleryImageHeight = resources.displayMetrics.heightPixels
        val height = (galleryImageHeight / 100) * 60
        Picasso.get().load(imgUrl)
            .resize(galleryImageWidth,height)
            .into(image, object :com.squareup.picasso.Callback{
            override fun onSuccess() {
                binding.progessBar.visibility = View.GONE
            }

            override fun onError(e: Exception?) {
                binding.progessBar.visibility = View.GONE
            }
        })
    }

    companion object{

        var NUM_GRID_COLUMNS = 5

        private val PERMISSION_CODE = 101
        // The permissions we need for the app to work properly
        private val PERMISSIONS = mutableListOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
        ).toTypedArray()
    }

    override fun onCreateLoader(id: Int, args: Bundle?): androidx.loader.content.Loader<Cursor> {
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        val selection: String? = null     //Selection criteria
        val selectionArgs = arrayOf<String>()  //Selection criteria
        val sortOrder: String? = null

        return CursorLoader(
            requireActivity().applicationContext,
            uri,
            projection,
            selection,
            selectionArgs,
            sortOrder)
    }

    override fun onLoadFinished(loader: androidx.loader.content.Loader<Cursor>, data: Cursor?) {
        data?.let {
            val columnIndexData = it.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

            while (it.moveToNext()) {

                listOfAllImages.add(it.getString(columnIndexData));
            }
            setupGridView(listOfAllImages)

        }
    }

    override fun onLoaderReset(loader: androidx.loader.content.Loader<Cursor>) {
    }
}