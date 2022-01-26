package com.example.tierdex.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tierdex.R
import com.example.tierdex.databinding.AnimalViewBinding
import com.example.tierdex.model.Animal

/**
 * This class implements a [RecyclerView] [ListAdapter] which uses Data Binding to present [List]
 * data, including computing diffs between lists.
 */
class AnimalAdapter
    : ListAdapter<Animal, AnimalAdapter.AnimalViewHolder>(DiffCallback) {

    class AnimalViewHolder ( private var binding: AnimalViewBinding )
        : RecyclerView.ViewHolder(binding.root){
            fun bind( animal : Animal ){
                binding.animal
                // This is important, because it forces the data binding to execute immediately,
                // which allows the RecyclerView to make the correct view size measurements
                binding.executePendingBindings()
            }
        }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of
     * [Animals] has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<Animal>() {
        override fun areItemsTheSame(oldItem: Animal, newItem: Animal): Boolean {
            return oldItem.animalID == newItem.animalID
        }

        override fun areContentsTheSame(oldItem: Animal, newItem: Animal): Boolean {
            return oldItem.imgSrcUrl == newItem.imgSrcUrl
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
       return AnimalViewHolder(
           AnimalViewBinding.inflate(LayoutInflater.from(parent.context))
       )
    }

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        val animal = getItem(position)
        holder.bind(animal)
    }

}