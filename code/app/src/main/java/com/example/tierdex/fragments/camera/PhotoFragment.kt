package com.example.tierdex.fragments.camera

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.tierdex.MainActivity
import com.example.tierdex.R
import com.example.tierdex.addDiscoveryFragment
import com.example.tierdex.databinding.FragmentPhotoBinding
import com.example.tierdex.model.PhotoViewModel
import kotlinx.android.synthetic.main.fragment_photo.view.*
import kotlinx.android.synthetic.main.item_view.view.*
import java.io.File

class PhotoFragment : Fragment() {

    private var _binding : FragmentPhotoBinding? = null
    private val binding get () = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val photoModel : PhotoViewModel by viewModels({requireParentFragment()})
        val uri = photoModel.selectedItem.value?.uri!!
        showPhoto(uri)

            binding.backButton.setOnClickListener{
                Navigation.findNavController(view).navigate(R.id.action_photo_view_pager_to_cameraLayout)
            }

            binding.shareButton.setOnClickListener{
                //create bundle with photo data
                val photo = uri.toString()
                val bundle = Bundle()
                bundle.putString("photo",photo)
                //send to addDiscoveryFragment
                val fragment = addDiscoveryFragment()
                fragment.arguments = bundle
                //navigate to addDiscoveryFragment
                Navigation.findNavController(view).navigate(R.id.action_photo_view_pager_to_addDiscoveryFragment)
            }
    }

    private fun showPhoto(uri: Uri){
        Glide.with(binding.photoViewPager)
            .load(uri)
            .into(binding.photoViewPager)
    }

    companion object{
        private const val FILE_NAME_KEY = "file_name"

        fun create(image: File) = PhotoFragment().apply {
            arguments = Bundle().apply {
                putString(FILE_NAME_KEY, image.absolutePath)
            }
        }
    }
}