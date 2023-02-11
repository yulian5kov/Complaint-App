package com.example.app_comp

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow

class UserViewModel : ViewModel()  {

    private val repo: FirestoreRepository = FirestoreRepository()

    //fun postComplaint(complaint: Complaint): Flow<Result<Unit>> = repo.postComplaint(complaint)
    fun postComplaint(complaint: Complaint): Flow<Result<Unit>> {
        Log.d(DEBUGGING, "Posting complaint: $complaint")
        val result = repo.postComplaint(complaint)
        Log.d(DEBUGGING, "Complaint posted")
        return result
    }

}