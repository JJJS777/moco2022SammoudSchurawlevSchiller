package com.example.tierdex.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.tierdex.adapter.AnimalAdapter
import com.example.tierdex.data.AnimalDataSource
import com.example.tierdex.databinding.FragmentAnimalListBinding
import com.example.tierdex.model.AnimalViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AnimalListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class AnimalListFragment : Fragment() {

    private lateinit var binding: FragmentAnimalListBinding
    private val viewModel: AnimalViewModel by viewModels()

    /**
     * Inflates the layout with Data Binding, sets its lifecycle owner to the OverviewFragment
     * to enable Data Binding to observe LiveData, and sets up the RecyclerView with an adapter.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAnimalListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = viewLifecycleOwner

        // Giving the binding access to the OverviewViewModel
        binding.viewModel = viewModel

        // Sets the adapter of the photosGrid RecyclerView
        binding.animalRecyclerView.adapter = AnimalAdapter()

        binding.animalSearch.setOnClickListener { onSearchAnimal() }

    }

    private fun onSearchAnimal() {
        val searchAnimal = binding.animalSearch.query.toString()
        viewModel.onSearch( searchAnimal )
    }
}