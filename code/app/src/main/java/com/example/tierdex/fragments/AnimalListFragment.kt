package com.example.tierdex.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.tierdex.adapter.AnimalAdapter
import com.example.tierdex.data.AnimalDataSource
import com.example.tierdex.databinding.FragmentAnimalListBinding
import com.example.tierdex.model.ItemAdapter

class AnimalListFragment: Fragment() {

    private var _binding: FragmentAnimalListBinding? = null
    private val binding get() = _binding!!
    private lateinit var rv: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAnimalListBinding.inflate( inflater, container, false )
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val myDataset = AnimalDataSource().loadAnimals()

        binding.recyclerView.adapter = AnimalAdapter(this, myDataset)
        binding.recyclerView.setHasFixedSize(true)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}