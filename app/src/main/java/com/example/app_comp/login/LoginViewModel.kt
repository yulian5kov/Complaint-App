package com.example.app_comp.login

import androidx.lifecycle.*
import com.example.app_comp.utils.FirestoreRepository
import com.example.app_comp.utils.Result
import com.example.app_comp.data.User
import kotlinx.coroutines.flow.Flow

class LoginViewModel : ViewModel() {
    fun loginUser(email: String, password: String): Flow<Result<User>> = FirestoreRepository().loginUser(email, password)
    fun addUser(user: User, password: String): Flow<Result<User>> = FirestoreRepository().addUser(user, password)
}