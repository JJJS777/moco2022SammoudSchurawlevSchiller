package com.example.tierdex.adapter

import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.tierdex.R
import com.example.tierdex.model.AnimalApiStatus
import com.example.tierdex.model.ApiResponse

/**
 * Updates the data shown in the [RecyclerView].
 */
@BindingAdapter("listData") //Attribute aus XML
fun bindRecyclerView(recyclerView: RecyclerView, data: ApiResponse) {
    val adapter = recyclerView.adapter as AnimalAdapter
    adapter.submitList(data.searchResults.results)
}


/**
 * Uses the Coil library to load an image by URL into an [ImageView]
 */
@BindingAdapter("imageUrl") //Attribute aus XML
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        imgView.load(imgUri) {
            placeholder(R.drawable.loading_animation)
            error(R.drawable.ic_broken_image)
        }
    }
}


@BindingAdapter("animalApiStatus") //Attribute aus XML
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
