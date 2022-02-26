package com.example.tierdex.model

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.tierdex.R
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class ItemAdapter(
    private val context: Fragment,
    private val _context : Context,
    private val dataset: List<Feed>) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    class ItemViewHolder( view: View ) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.ProfileTitle)
        val imageView : ImageView = view.findViewById(R.id.ivPost)
        val shareButton : ImageButton = view.findViewById(R.id.btnShare)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // create a new View
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.item_view,
            parent,false)
        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        holder.textView.text = item.imageID
        Picasso.get().load(item.imageUrl).into(holder.imageView)

        holder.shareButton.setOnClickListener {
            shareImage(item.imageUrl,holder)
        }
    }

    private fun bitmapUri(holder: ItemViewHolder): Uri {
        val drawable = holder.imageView.drawable
        val mBitmap = (drawable as BitmapDrawable).bitmap

        val path = MediaStore.Images.Media
            .insertImage(_context.contentResolver, mBitmap,
                "Hi, look what I found in TierDex", null)

        return Uri.parse(path)
    }

    private fun shareImage(uri : String,holder: ItemViewHolder){
        Picasso.get().load(uri).into(object  : Target {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {

                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "image/*"
                    putExtra(Intent.EXTRA_STREAM,bitmapUri(holder))
                }
                context.startActivity(Intent.createChooser(intent, "Share File"));
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                TODO("Not yet implemented")
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun getItemCount(): Int  = dataset.size
}