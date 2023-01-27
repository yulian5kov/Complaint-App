package com.example.app_comp

import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _addUserEvent = MutableLiveData<Event<Unit>>()
    val addUserEvent: LiveData<Event<Unit>> = _addUserEvent
    private val firestoreRepository = FirestoreRepository()
    private val _loginEvent = MutableLiveData<Event<Unit>>()
    val loginEvent: LiveData<Event<Unit>>
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