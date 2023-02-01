package com.example.app_comp

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow

class UserViewModel : ViewModel()  {

    private val repo: FirestoreRepository = FirestoreRepository()

    fun postComplaint(complaint: String): Flow<Result<Unit>> = repo.postComplaint(complaint)
    fun getComplaints(userId: String): Flow<Result<List<Complaint>>>  = repo.getComplaints(userId)
    //userId: String - userId
}