package com.example.app_comp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ComplaintAdapter(private var complaints: List<Complaint>) :
    RecyclerView.Adapter<ComplaintAdapter.ComplaintViewHolder>() {

    inner class ComplaintViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTitle: TextView = itemView.findViewById(R.id.complaint_title)
        val textViewDescription: TextView = itemView.findViewById(R.id.complaint_description)
        val textViewStatus: TextView = itemView.findViewById(R.id.complaint_status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplaintViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ComplaintViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ComplaintViewHolder, position: Int) {
        val currentComplaint = complaints[position]
        holder.textViewTitle.text = currentComplaint.title
        holder.textViewDescription.text = currentComplaint.date.toString()
        holder.textViewStatus.text = currentComplaint.status
        holder.itemView.setOnClickListener {

        }
    }

    override fun getItemCount() = complaints.size

    fun setComplaints(complaints: List<Complaint>) {
        this.complaints = complaints
        notifyDataSetChanged()
    }
}
