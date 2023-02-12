package com.example.app_comp

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app_comp.databinding.ItemComplaintBinding

class ComplaintAdapter(private val complaints: List<Complaint>) : RecyclerView.Adapter<ComplaintAdapter.ComplaintViewHolder>() {

    class ComplaintViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val complaintTitleTextView: TextView = itemView.findViewById(R.id.complaint_title_text_view)
        val complaintDescriptionTextView: TextView = itemView.findViewById(R.id.complaint_description_text_view)
        val imageView: ImageView = itemView.findViewById(R.id.image_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplaintViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_complaint, parent, false)
        return ComplaintViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return complaints.size
    }

    override fun onBindViewHolder(holder: ComplaintViewHolder, position: Int) {
        val currentComplaint = complaints[position]
        holder.complaintTitleTextView.text = currentComplaint.title
        holder.complaintDescriptionTextView.text = currentComplaint.description
        //holder.imageView.setImageBitmap(currentComplaint.images)
        if (currentComplaint.images.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(currentComplaint.images[0])
                .into(holder.imageView)
        }
    }
}



