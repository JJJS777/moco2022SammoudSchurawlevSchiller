package com.example.tierdex.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tierdex.adapter.AnimalAdapter
import com.example.tierdex.data.AnimalDataSource
import com.example.tierdex.databinding.FragmentAnimalListBinding

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
    // TODO: Rename and change types of parameters
    private var _binding: FragmentAnimalListBinding? = null
    private val binding get() = _binding!!
    private lateinit var  rv: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAnimalListBinding.inflate( inflater, container, false)
        return binding.root
        //return inflater.inflate(R.layout.fragment_animal_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myData = AnimalDataSource().loadAnimals()

        rv = binding.recyclerView
        rv.adapter = AnimalAdapter( this, myData )
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AnimalListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AnimalListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}