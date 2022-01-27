package com.example.tierdex.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.tierdex.R

class ItemAdapter(
    private val context: Fragment,
    private val dataset: List<Post>) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    class ItemViewHolder( view: View ) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.ProfileTitle)
        val imageView : ImageView = view.findViewById(R.id.ivPost)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // create a new View
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.item_view,
            parent,false)
        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        holder.textView.text = context.resources.getString(item.userID)
        holder.imageView.setImageResource(item.imageResourceId)
    }

    override fun getItemCount(): Int  = dataset.size

}