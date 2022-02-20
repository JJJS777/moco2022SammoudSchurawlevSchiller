package com.example.tierdex

import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.tierdex.data.entities.Discovered
import com.bumptech.glide.Glide
import com.example.tierdex.databinding.AddDiscoveryFragmentBinding
import com.example.tierdex.databinding.FragmentPhotoBinding
import kotlinx.android.synthetic.main.add_discovery_fragment.view.*

class addDiscoveryFragment : Fragment() {

    companion object {
        fun newInstance() = addDiscoveryFragment()
    }

    lateinit var disco: Discovered
    lateinit var binding : AddDiscoveryFragmentBinding

    // Use the 'by activityViewModels()' Kotlin property delegate from the fragment-ktx artifact
    // to share the ViewModel across fragments.
    private val viewModel: AddDiscoveryViewModel by activityViewModels {
        AddDiscoveryViewModelFactory(
            (activity?.application as TierDexApplication).database.discoveredDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    private var uri : String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddDiscoveryFragmentBinding.inflate(inflater)

        //toDo
        /*binding.btnCamera.setOnClickListener{Navigation.findNavController()
            .navigate(R.id.action_addDiscoveryFragment_to_cameraLayout)}*/

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProvider(this).get(AddDiscoveryViewModel::class.java)
        // TODO: Use the ViewModel
    }

}


