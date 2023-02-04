package com.example.app_comp

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow

class UserViewModel : ViewModel()  {

    private val repo: FirestoreRepository = FirestoreRepository()

    fun postComplaint(complaint: Complaint) {
        repo.postComplaint(complaint)
    }

}