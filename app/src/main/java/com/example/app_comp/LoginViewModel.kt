package com.example.app_comp

import androidx.lifecycle.*

class LoginViewModel : ViewModel() {
    private var repo = FirestoreRepository()

    fun loginUser(email: String, password: String): LiveData<Result<User>> {
        return repo.loginUser(email, password).asLiveData()
    }

    fun addUser(user: User, password: String): LiveData<Result<User>> {
        return repo.addUser(user, password).asLiveData()
    }
}