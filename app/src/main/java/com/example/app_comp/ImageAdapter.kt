package com.example.app_comp

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app_comp.data.FileData
import com.example.app_comp.databinding.ItemFileBinding
import com.example.app_comp.databinding.ItemImageBinding

class ImageAdapter(private val context: Context, private val imageUris: List<Uri>) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(private val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener  {

        init {
            binding.imageView.setOnClickListener(this)
        }

        fun bind(imageUri: Uri) {
            Glide.with(context)
                .load(imageUri)
                .into(binding.imageView)
        }

        override fun onClick(view: View?) {
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val imageUri = imageUris[position]
                val fullscreenFragment = ImageFullscreenFragment.newInstance(imageUri)
                val fragmentManager = (context as FragmentActivity).supportFragmentManager
                val transaction = fragmentManager.beginTransaction()
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                transaction.add(android.R.id.content, fullscreenFragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemImageBinding.inflate(layoutInflater, parent, false)
        return ImageViewHolder(binding)
    }

    override fun getItemCount(): Int = imageUris.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUri = imageUris[position]
        holder.bind(imageUri)
    }

}

