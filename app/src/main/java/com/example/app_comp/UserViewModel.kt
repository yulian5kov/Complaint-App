package com.example.app_comp

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow

class UserViewModel : ViewModel()  {

    private val repo: FirestoreRepository = FirestoreRepository()

    fun postComplaint(text: String, images: List<Uri>) {
        repo.postComplaint(text, images)
    }

}