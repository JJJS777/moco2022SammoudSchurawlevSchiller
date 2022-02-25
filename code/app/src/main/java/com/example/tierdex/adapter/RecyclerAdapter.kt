package com.example.tierdex.model

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.tierdex.R
import com.example.tierdex.data.entities.Discovered
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception

class ItemAdapter(
    private val context: Fragment,
    private val dataset: LiveData<List<Discovered>>) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

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
        val item = dataset.value!![position]
        //holder.textView.text = context.resources.getString(item.userID)
        Picasso.get().load(item.imageUrl).into(holder.imageView)

        holder.shareButton.setOnClickListener {
            shareImage(item.imageUrl!!)
        }
    }

    private fun shareImage(uri : String){
        Picasso.get().load(uri).into(object  : Target {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "image/*"
                    putExtra(Intent.EXTRA_STREAM,getBibmap(bitmap))
                }
                context.startActivity(Intent.createChooser(intent,"Share Image"))
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                TODO("Not yet implemented")
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun getItemCount(): Int  = dataset.value!!.size

    private fun getBibmap(bitmap: Bitmap?) : Uri?{
        var bmpUri : Uri? = null
        try {
            val file = File(System.currentTimeMillis().toString() + ".jpg")
            val out = FileOutputStream(file)
            bitmap?.compress(Bitmap.CompressFormat.JPEG,90,out)
            out.close()
            bmpUri = Uri.fromFile(file)
        }catch (e: IOException){
            e.printStackTrace()
        }

        return bmpUri
    }
}