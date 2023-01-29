package com.example.app_comp

sealed class Result<out T> {
    data class Loading(val isLoading: Boolean) : Result<Nothing>()
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: String) : Result<Nothing>()
    data class Failed(val message: Int, val error: String) : Result<Nothing>()
}