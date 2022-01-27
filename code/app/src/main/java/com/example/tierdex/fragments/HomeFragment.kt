package com.example.tierdex.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tierdex.databinding.FragmentHomeBinding
import com.example.tierdex.data.DataSource
import com.example.tierdex.model.ItemAdapter


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myDataset = DataSource().loadPost()

        binding.recyclerView.adapter = ItemAdapter(this, myDataset)
        binding.recyclerView.setHasFixedSize(true)

    }
}