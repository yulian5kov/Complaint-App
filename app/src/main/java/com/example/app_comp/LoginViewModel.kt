package com.example.app_comp

import androidx.lifecycle.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _addUserEvent = MutableLiveData<Event<User>>()
    private val firestoreRepository = FirestoreRepository()
    private val _loginEvent = MutableLiveData<Event<User>>()
    val loginEvent: LiveData<Event<User>>
        get() = _loginEvent

    fun addUser(user: User, password: String) {
        _addUserEvent.value = Event.Loading(true)
        viewModelScope.launch {
            firestoreRepository.addUser(user, password).collect { event ->
                _addUserEvent.value = event
            }
        }
    }

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            _loginEvent.value = Event.Loading(true)
            val event = firestoreRepository.loginUser(email, password).first()
            _loginEvent.value = event
        }
    }

}