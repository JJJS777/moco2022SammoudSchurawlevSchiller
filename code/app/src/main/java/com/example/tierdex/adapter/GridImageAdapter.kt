package com.example.tierdex.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ProgressBar
import com.example.tierdex.R
import com.squareup.picasso.Picasso
import java.io.File
import java.lang.Exception

data class ViewHolder(
    var image: ImageView,
    var progressBar : ProgressBar
)

class GridImageAdapter(
    private val mContext: Context,
    private val layoutResource : Int,
    private val imageUrls : ArrayList<String>)
    : ArrayAdapter<String>(mContext,layoutResource,imageUrls) {


    private lateinit var viewHolder: ViewHolder

    @SuppressLint("CutPasteId", "ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val mInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val returnView = mInflater.inflate(layoutResource,parent,false)

        viewHolder = ViewHolder(
            returnView.findViewById<ImageView>(R.id.gridImageView),
            returnView.findViewById<ProgressBar>(R.id.gridImageProgressbar))


        val imgURL = getItem(position)
        if(imgURL != null){
            val file = File(getItem(position)!!)
            val uri : Uri = Uri.fromFile(file)

            Picasso.get().load(uri)
                .resize(288,200)
                .into(viewHolder.image,object : com.squareup.picasso.Callback{
                override fun onSuccess() {
                    viewHolder.progressBar.visibility = View.GONE
                }

                override fun onError(e: Exception?) {
                    Log.d("GRIDVIEW",e.toString())
                    viewHolder.progressBar.visibility = View.GONE
                }
            })
        }

        return returnView!!
    }
}