package com.example.app_comp

sealed class ResultData<out T> {
    data class Success<out T>(val data: T? = null): ResultData<T>()
    data class Loading(val nothing: Nothing? = null): ResultData<Nothing>()
    data class Failed(val message: Int, val error: String): ResultData<Nothing>()
    data class Exception(val exception: kotlin.Exception? = null): ResultData<Nothing>()
}