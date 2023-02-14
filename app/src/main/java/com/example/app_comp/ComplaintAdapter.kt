package com.example.app_comp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

//private val onClickListener: OnClickListener
class ComplaintAdapter(private var complaints: List<Complaint>) :
    RecyclerView.Adapter<ComplaintAdapter.ComplaintViewHolder>() {

    //private var complaints = emptyList<Complaint>()

    inner class ComplaintViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        fun bind(complaint: Complaint) {
//            itemView.title_text.text = complaint.title
//            itemView.description_text.text = complaint.description
//            itemView.setOnClickListener {
//                onClickListener.onClick(complaint)
//            }
//        }
        val textViewTitle: TextView = itemView.findViewById(R.id.complaint_title)
        val textViewDescription: TextView = itemView.findViewById(R.id.complaint_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplaintViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ComplaintViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ComplaintViewHolder, position: Int) {
        val currentComplaint = complaints[position]
        //holder.bind(currentComplaint)
        holder.textViewTitle.text = currentComplaint.title
        holder.textViewDescription.text = currentComplaint.description
    }

    override fun getItemCount() = complaints.size

    fun setComplaints(complaints: List<Complaint>) {
        this.complaints = complaints
        notifyDataSetChanged()
    }

    class OnClickListener(val clickListener: (complaint: Complaint) -> Unit) {
        fun onClick(complaint: Complaint) = clickListener(complaint)
    }
}
