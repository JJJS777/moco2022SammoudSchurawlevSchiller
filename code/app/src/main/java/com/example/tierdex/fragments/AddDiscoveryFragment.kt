package com.example.tierdex.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.tierdex.AddDiscoveryViewModel
import com.example.tierdex.AddDiscoveryViewModelFactory
import com.example.tierdex.R
import com.example.tierdex.TierDexApplication
import com.example.tierdex.databinding.AddDiscoveryFragmentBinding


class AddDiscoveryFragment : Fragment() {

    lateinit var binding : AddDiscoveryFragmentBinding
    private var uri : String? = null


    // Use the 'by activityViewModels()' Kotlin property delegate from the fragment-ktx artifact
    // to share the ViewModel across fragments.
    private val viewModel: AddDiscoveryViewModel by activityViewModels {
        AddDiscoveryViewModelFactory(
            (activity?.application as TierDexApplication).database.discoveredDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddDiscoveryFragmentBinding.inflate(inflater)
        return binding.root
    }

    /**
     * Inserts the new Item into database and navigates up to list fragment.
     */
    private fun addNewDisco() {
        viewModel.addNewDiscovery(
            binding.textInputAnimalName.text.toString()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        uri = requireArguments().getString("photo")

        binding.btnCamera.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_addDiscoveryFragment_to_cameraLayout)
        }

            binding.saveAction.setOnClickListener {
                addNewDisco()
            }
    }
}


