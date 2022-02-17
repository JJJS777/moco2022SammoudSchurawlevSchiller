package com.example.tierdex

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
import com.example.tierdex.databinding.AddDiscoveryFragmentBinding
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
            (activity?.application as AddDiscoveryApplication).database.discoveredDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

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

        super.onViewCreated(view, savedInstanceState)

            binding.saveAction.setOnClickListener {
                addNewDisco()
            }
    }


}


