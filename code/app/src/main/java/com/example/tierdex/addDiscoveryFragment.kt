package com.example.tierdex

import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavGraph
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.tierdex.databinding.AddDiscoveryFragmentBinding
import com.example.tierdex.databinding.FragmentPhotoBinding
import kotlinx.android.synthetic.main.add_discovery_fragment.view.*

class addDiscoveryFragment : Fragment() {

    companion object {
        fun newInstance() = addDiscoveryFragment()
    }

    private var _binding : AddDiscoveryFragmentBinding? = null
    private val binding get () = _binding!!

    private lateinit var viewModel: AddDiscoveryViewModel

    private var uri : String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddDiscoveryFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        uri = arguments!!.getString("photo")

        binding.btnCamera.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_addDiscoveryFragment_to_cameraLayout)
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddDiscoveryViewModel::class.java)
        // TODO: Use the ViewModel
    }


}


