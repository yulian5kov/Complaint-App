package com.example.app_comp

import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ComplaintAdapter(private var complaints: List<Complaint>, private val isAdmin: Boolean = false) :
    RecyclerView.Adapter<ComplaintAdapter.ComplaintViewHolder>() {

    private var onItemClickCallback: ((Complaint) -> Unit)? = null

    fun setOnItemClickCallback(callback: (Complaint) -> Unit) {
        onItemClickCallback = callback
    }

    inner class ComplaintViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTitle: TextView = itemView.findViewById(R.id.complaint_title)
        val textViewDescription: TextView = itemView.findViewById(R.id.complaint_description)
        val complaintStatus: TextView = itemView.findViewById(R.id.complaint_status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplaintViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ComplaintViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ComplaintViewHolder, position: Int) {
        val complaint = complaints[position]
        holder.textViewTitle.text = complaint.title
        holder.textViewDescription.text = complaint.date.toString()
        holder.complaintStatus.text = complaint.status

        // If user is not an admin, disable editing of the status field
        if (!isAdmin) {
            holder.complaintStatus.isEnabled = false
        } else {
            holder.complaintStatus.isEnabled = true
            holder.complaintStatus.setOnClickListener {
                // create a dialog for the admin to update the status
                val alertDialog = AlertDialog.Builder(holder.itemView.context)
                alertDialog.setTitle("Update Status")
                alertDialog.setMessage("Enter the new status for this complaint")
                val input = EditText(holder.itemView.context)
                alertDialog.setView(input)
                alertDialog.setPositiveButton("Update") { dialog, _ ->
                    val newStatus = input.text.toString()
                    complaint.status = newStatus
                    updateComplaintStatus(complaint.id, newStatus)
                    notifyDataSetChanged()
                    dialog.dismiss()
                }
                alertDialog.setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                alertDialog.show()
            }
        }

        holder.itemView.setOnClickListener {
            Log.d(DEBUGGING, "onItemClickCallback = $onItemClickCallback")
            if(onItemClickCallback != null){
                Log.d(DEBUGGING, "strugare")
                onItemClickCallback?.invoke(complaint) // add null check here
            }
        }
    }

    override fun getItemCount() = complaints.size

    fun setComplaints(complaints: List<Complaint>) {
        this.complaints = complaints
        notifyDataSetChanged()
    }

    interface OnComplaintStatusUpdatedListener {
        fun onComplaintStatusUpdated(complaint: Complaint)
    }

    private var onComplaintStatusUpdatedListener: OnComplaintStatusUpdatedListener? = null

    fun setOnComplaintStatusUpdatedListener(listener: OnComplaintStatusUpdatedListener) {
        onComplaintStatusUpdatedListener = listener
    }

    private fun updateComplaintStatus(id: String, status: String) {
        db.collection("complaints").document(id)
            .update("status", status)
            .addOnSuccessListener {
                val updatedComplaint = complaints.find { it.id == id }
                if (updatedComplaint != null) {
                    updatedComplaint.status = status
                    notifyDataSetChanged()

                    onComplaintStatusUpdatedListener?.onComplaintStatusUpdated(updatedComplaint)
                }
            }
            .addOnFailureListener { e ->
                Log.d(DEBUGGING, "Error updating document", e)
            }
    }
}

