package com.example.tierdex

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class addDiscoveryFragment : Fragment() {

    companion object {
        fun newInstance() = addDiscoveryFragment()
    }

    private lateinit var viewModel: AddDiscoveryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_discovery_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddDiscoveryViewModel::class.java)
        // TODO: Use the ViewModel
    }

}