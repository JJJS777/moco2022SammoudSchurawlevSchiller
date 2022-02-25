package com.example.tierdex.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tierdex.AddDiscoveryViewModel
import com.example.tierdex.AddDiscoveryViewModelFactory
import com.example.tierdex.TierDexApplication
import com.example.tierdex.data.dao.DiscoveredDao
import com.example.tierdex.data.entities.Discovered
import com.example.tierdex.databinding.FragmentHomeBinding
import com.example.tierdex.model.Feed
import com.example.tierdex.model.HomeFragmentViewModel
import com.example.tierdex.model.ItemAdapter
import com.google.android.gms.tasks.Task
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // Use the 'by activityViewModels()' Kotlin property delegate from the fragment-ktx artifact
    // to share the ViewModel across fragments.
    private val viewModel: HomeFragmentViewModel by activityViewModels {
        AddDiscoveryViewModelFactory(
            (activity?.application as TierDexApplication).database.discoveredDao()
        )
    }

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
        loadPost()

    }


    private fun loadPost(){
        val storage = Firebase.storage
        val storageRef = storage.reference.child("images")
        val feedList : LiveData<List<Discovered>>

        val listAllTask : Task<ListResult> = storageRef.listAll()

        if (checkInternetState()){

            listAllTask.addOnCompleteListener { result ->
                val items : List<StorageReference> = result.result!!.items

                items.forEachIndexed { index, item ->
                    item.downloadUrl.addOnSuccessListener {
                        feedList.value!!.toList()
                    }.addOnCompleteListener {
                        binding.feedRecyclerView.adapter = ItemAdapter(this,feedList)
                        binding.feedRecyclerView.layoutManager = LinearLayoutManager(context)
                    }
                }
            }
        } else {
            //ToDo wenn internet aus firestore wenn nicht aus room lasen mit hilfe von ViewModel
            val items: LiveData<List<Discovered>> = viewModel.allDiscoveries

            binding.feedRecyclerView.adapter = ItemAdapter(this, items)
        }

    }

    private fun checkInternetState() : Boolean{
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            // Representation of the capabilities of an active network.
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                // Indicates this network uses a Wi-Fi transport,
                // or WiFi has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                // Indicates this network uses a Cellular transport. or
                // Cellular has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        }else{
            return false
        }
    }
}