package com.example.tierdex.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tierdex.databinding.FragmentHomeBinding
import com.example.tierdex.model.Feed
import com.example.tierdex.model.ItemAdapter
import com.google.android.gms.tasks.Task
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage


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

        loadPost()
    }

    private fun loadPost(){
        val storage = Firebase.storage
        val storageRef = storage.reference.child("images")
        val feedList : ArrayList<Feed> = ArrayList()

        val listAllTask : Task<ListResult> = storageRef.listAll()
        listAllTask.addOnCompleteListener { result ->
            val items : List<StorageReference> = result.result!!.items

            items.forEachIndexed { index, item ->
                item.downloadUrl.addOnSuccessListener {
                    feedList.add(Feed("User", it.toString()))
                }.addOnCompleteListener {
                    binding.feedRecyclerView.adapter = ItemAdapter(this,feedList)
                    binding.feedRecyclerView.layoutManager = LinearLayoutManager(context)
                }
            }
        }
    }
}