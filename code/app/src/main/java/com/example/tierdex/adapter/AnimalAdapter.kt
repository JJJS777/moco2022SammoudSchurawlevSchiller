package com.example.tierdex.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tierdex.databinding.AnimalViewBinding
import com.example.tierdex.model.AnimalData

/**
 * This class implements a [RecyclerView] [ListAdapter] which uses Data Binding to present [List]
 * data, including computing diffs between lists.
 */
class AnimalAdapter
    : ListAdapter<AnimalData, AnimalAdapter.AnimalViewHolder>(DiffCallback) {

    class AnimalViewHolder ( private var binding: AnimalViewBinding )
        : RecyclerView.ViewHolder(binding.root){
            fun bind( animalData : AnimalData ){
                binding.animal = animalData
                // This is important, because it forces the data binding to execute immediately,
                // which allows the RecyclerView to make the correct view size measurements
                binding.executePendingBindings()
            }
        }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of
     * [Animals] has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<AnimalData>() {
        override fun areItemsTheSame(oldItem: AnimalData, newItem: AnimalData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: AnimalData, newItem: AnimalData): Boolean {
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