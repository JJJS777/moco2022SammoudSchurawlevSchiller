package com.example.tierdex.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.tierdex.R
import com.example.tierdex.model.Animal

class AnimalAdapter( private val context: Fragment, private val animalDataSet: List<Animal> )
    : RecyclerView.Adapter<AnimalAdapter.AnimalViewHolder>() {

    class AnimalViewHolder( view: View )
        : RecyclerView.ViewHolder( view ){

        val textView: TextView = view.findViewById( R.id.animal_title )
        val imageView: ImageView = view.findViewById( R.id.animal_image )

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
       val adapterLayout = LayoutInflater.from(parent.context)
           .inflate( R.layout.animal_view, parent, false )

       return AnimalViewHolder( adapterLayout )
    }

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        val current = animalDataSet.get(position)
        holder.textView.text = context.resources.getString( current.animalTitle )
        holder.imageView.setImageResource( current.imgRes )
    }

    override fun getItemCount(): Int = animalDataSet.size
}