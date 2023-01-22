package com.example.app_comp

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import androidx.lifecycle.asLiveData

class LoginViewModel : ViewModel() {
    fun addUser(user: User, password: String): LiveData<User> {
        return FirebaseProfileService.addUser(user, password).asLiveData()
    }
}