package com.example.app_comp

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow

class UserViewModel : ViewModel()  {

    private val repo: FirestoreRepository = FirestoreRepository()

    fun postComplaint(complaint: Complaint): Flow<Result<Unit>> = repo.postComplaint(complaint)
}