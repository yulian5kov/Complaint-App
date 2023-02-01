package com.example.app_comp.login

import androidx.lifecycle.*
import com.example.app_comp.FirestoreRepository
import com.example.app_comp.Result
import com.example.app_comp.User
import kotlinx.coroutines.flow.Flow

class LoginViewModel : ViewModel() {
    private var repo = FirestoreRepository()

    fun loginUser(email: String, password: String): Flow<Result<User>> = repo.loginUser(email, password)

    fun addUser(user: User, password: String): Flow<Result<User>> = repo.addUser(user, password)
}