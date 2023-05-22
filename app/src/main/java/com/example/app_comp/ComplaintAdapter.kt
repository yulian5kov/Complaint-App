package com.example.app_comp

import android.app.Activity
import java.util.Calendar
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.app_comp.data.Complaint
import com.example.app_comp.databinding.ItemComplaintBinding
import com.example.app_comp.utils.DEBUGGING
import java.util.Locale

class ComplaintAdapter(private var complaints: List<Complaint>, private val activity: FragmentActivity) :
    RecyclerView.Adapter<ComplaintAdapter.ComplaintViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(complaint: Complaint)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }


    inner class ComplaintViewHolder(val binding: ItemComplaintBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplaintViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemComplaintBinding.inflate(layoutInflater, parent, false)
        return ComplaintViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return complaints.size
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: ComplaintViewHolder, position: Int) {
        holder.binding.apply {
            val complaint = complaints[position]
            val timestamp = complaint.date
            val date = timestamp.toDate()
            tvTitle.text = complaint.title
            tvDesc.text = complaint.description

            // Extracting the day, month, and year from the date
            val calendar = Calendar.getInstance()
            calendar.time = date
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())
            val year = calendar.get(Calendar.YEAR)

            tvDeliveryDate.text = day.toString()
            tvDeliveryMonth.text = month
            tvDeliveryYear.text = year.toString()

            // Set click listener on the item view
            root.setOnClickListener {
//                val complaintId = complaint.id
//                val fragment = ComplaintDetailsFragment.newInstance(complaintId)
//                activity.supportFragmentManager
//                    .beginTransaction()
//                    .replace(R.id.frame_layout, fragment)
//                    .addToBackStack(null)
//                    .commit()
                itemClickListener?.onItemClick(complaint)
            }

        }
    }

    fun updateComplaints(newComplaints: List<Complaint>) {
        complaints = newComplaints
        notifyDataSetChanged()
    }
}
