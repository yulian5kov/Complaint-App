package com.example.app_comp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_comp.databinding.ListItemComplaintBinding

class ComplaintAdapter(var complaints: List<Complaint>) : RecyclerView.Adapter<ComplaintAdapter.ViewHolder>() {

    class ViewHolder(val view: ListItemComplaintBinding) : RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = ListItemComplaintBinding.inflate(inflater, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.textViewComplaint.text = complaints[position].text
        holder.view.textViewDate.text = complaints[position].date.toString()
    }

    override fun getItemCount(): Int = complaints.size
}
