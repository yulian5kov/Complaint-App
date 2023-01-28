package com.example.app_comp

sealed class Event<out T> {
    data class Loading(val isLoading: Boolean) : Event<Nothing>()
    data class Success<out T>(val data: T) : Event<T>()
    data class Error(val exception: String) : Event<Nothing>()
    data class Failed(val message: Int, val error: String) : Event<Nothing>()
}