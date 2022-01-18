/*
 * Copyright (C) 2021 The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.inventory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.example.tierdex.databinding.FragmentAddDiscoveryBinding
import android.content.Context.INPUT_METHOD_SERVICE
import androidx.lifecycle.ViewModelProvider
import com.example.tierdex.TierDexViewModel
import com.example.tierdex.data.entities.Animals
import com.example.tierdex.data.entities.Discovered


/**
 * Fragment to add or update an item in the Inventory database.
 */
class AddDiscoveryFragment : Fragment() {

    //private val navigationArgs: ItemDetailFragmentArgs by navArgs()

//    private val viewModel: TierDexViewModel by activityViewModels {
//        TierDexViewModelFactory(
//            (activity?.application as TierDexApplication).database.animalDao(),
//            (activity?.application as TierDexApplication).database.discoveredDao()
//        )
//    }

    lateinit var animals: Animals
    lateinit var discovery: Discovered

    private val viewModel: TierDexViewModel = ViewModelProvider(this)
        .get( TierDexViewModel::class.java)

    //TODO mit Data Binding ersetzten
    private var _binding: FragmentAddDiscoveryBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddDiscoveryBinding.inflate(inflater, container, false)
        return binding.root
    }

    //TODO private fun isEntryValid(): Boolean { } implementieren

    private fun addNewDiscovery(){
        //ToDO hier isEntryValid() aufrufen
        viewModel.addNewDiscovery(
            binding.discoveryTime.text.toString(),
            binding.discoveryIsAlive.text.toString()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.saveAction.setOnClickListener {
            addNewDiscovery()
        }
    }

    /**
     * Called before fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        // Hide keyboard.
        val inputMethodManager = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }
}
