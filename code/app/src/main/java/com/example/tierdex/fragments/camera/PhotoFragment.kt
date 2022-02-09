package com.example.tierdex.fragments.camera

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.tierdex.databinding.FragmentPhotoBinding
import com.example.tierdex.model.PhotoViewModel

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

        binding.backButton.setOnClickListener {
        }

        binding.shareButton.setOnClickListener {

        }




    }

    private fun showPhoto(uri: Uri){
        Glide.with(binding.photoViewPager)
            .load(uri)
            .into(binding.photoViewPager)
    }

}