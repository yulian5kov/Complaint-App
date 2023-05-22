package com.example.app_comp

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.app_comp.data.Complaint
import com.example.app_comp.data.User
import com.example.app_comp.utils.DEBUGGING
import com.example.app_comp.utils.FirestoreRepository
import com.example.app_comp.utils.Result
import kotlinx.coroutines.flow.Flow

class AdminViewModel : ViewModel()  {

    fun getAllComplaints(): Flow<Result<List<Complaint>>> = FirestoreRepository().getAllComplaints()
    fun updateComplaintStatus(complaintId: String, newStatus: String)
    : Flow<Result<Unit>> = FirestoreRepository().updateComplaintStatus(complaintId,newStatus)

}