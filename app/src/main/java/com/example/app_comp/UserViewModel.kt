package com.example.app_comp

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.app_comp.data.Complaint
import com.example.app_comp.data.User
import com.example.app_comp.utils.DEBUGGING
import com.example.app_comp.utils.FirestoreRepository
import com.example.app_comp.utils.Result
import kotlinx.coroutines.flow.Flow

class UserViewModel : ViewModel()  {

    fun postComplaint(complaint: Complaint): Flow<Result<Complaint>> = FirestoreRepository().postComplaint(complaint)
    fun getComplaintsByUser(userId: String): Flow<Result<List<Complaint>>> = FirestoreRepository().getComplaintsByUser(userId)
    fun getComplaintById(complaintId: String): Flow<Result<Complaint>> = FirestoreRepository().getComplaintById(complaintId)
}