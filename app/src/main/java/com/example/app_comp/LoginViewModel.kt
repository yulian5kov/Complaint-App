package com.example.app_comp

import androidx.lifecycle.*
import kotlinx.coroutines.flow.Flow

class LoginViewModel : ViewModel() {
    private var repo = FirestoreRepository()

    fun loginUser(email: String, password: String): Flow<Result<User>> = repo.loginUser(email, password)

    fun addUser(user: User, password: String): Flow<Result<User>> = repo.addUser(user, password)
}