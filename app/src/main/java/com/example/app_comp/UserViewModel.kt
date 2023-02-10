package com.example.app_comp

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow

class UserViewModel : ViewModel()  {

    private val repo: FirestoreRepository = FirestoreRepository()
//    private val _postComplaintResult = MutableLiveData<Result<Unit>>()
//    val postComplaintResult: LiveData<Result<Unit>>
//        get() = _postComplaintResult
//
//    fun postComplaint(complaint: Complaint) {
//        db.postComplaint(complaint).observe(viewLifecycleOwner, Observer { result ->
//            when (result) {
//                is Result.Loading -> {
//                    // Show loading
//                }
//                is Result.Success -> {
//                    // Show success
//                }
//                is Result.Error -> {
//                    // Show error
//                }
//                is Result.Failed -> {
//                    // Show failed
//                }
//            }
//        })
//    }

    fun postComplaint(complaint: Complaint): Flow<Result<Unit>> = repo.postComplaint(complaint)
}