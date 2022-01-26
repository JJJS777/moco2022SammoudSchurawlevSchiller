package com.example.tierdex.adapter

import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.tierdex.R
import com.example.tierdex.model.Animal
import com.example.tierdex.model.AnimalApiStatus


@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Animal>?) {
    val adapter = recyclerView.adapter as AnimalAdapter
    adapter.submitList(data)
}


/**
 * Uses the Coil library to load an image by URL into an [ImageView]
 */
@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        imgView.load(imgUri) {
            placeholder(R.drawable.loading_animation)
            error(R.drawable.ic_broken_image)
        }
    }
}


@BindingAdapter("AnimalApiStatus")
fun bindStatus(statusImageView: ImageView, status: AnimalApiStatus?) {
    when (status) {
        AnimalApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        AnimalApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        AnimalApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }
}
