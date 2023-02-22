package com.example.app_comp

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ImageAdapter(private val context: Context) :
    RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    private val images = mutableListOf<Uri>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.image_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageUri = images[position]
        holder.bind(imageUri)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    fun addImage(imageUri: Uri) {
        images.add(imageUri)
        notifyItemInserted(images.size - 1)
    }

    fun getImages(): List<String> {
        val paths = mutableListOf<String>()
        for (imageUri in images) {
            paths.add(imageUri.toString())
        }
        return paths
    }



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivImage: ImageView = itemView.findViewById(R.id.image_view)

        fun bind(imageUri: Uri) {
            Glide.with(itemView).load(imageUri).into(ivImage)
        }
    }
}
