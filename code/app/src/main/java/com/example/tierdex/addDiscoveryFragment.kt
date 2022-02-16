package com.example.tierdex

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavGraph
import androidx.navigation.Navigation
import com.example.tierdex.databinding.AddDiscoveryFragmentBinding
import kotlinx.android.synthetic.main.add_discovery_fragment.view.*

class addDiscoveryFragment : Fragment() {

    companion object {
        fun newInstance() = addDiscoveryFragment()
    }

    private lateinit var viewModel: AddDiscoveryViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.add_discovery_fragment, container, false)
        view.btnCamera.setOnClickListener{Navigation.findNavController(view).navigate(R.id.action_addDiscoveryFragment_to_cameraLayout)}
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lateinit var binding : AddDiscoveryFragmentBinding

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddDiscoveryViewModel::class.java)
        // TODO: Use the ViewModel
    }

}


