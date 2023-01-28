package com.example.app_comp

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class Config(context: Context) {
    val sharedPreferences: SharedPreferences? = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
    companion object {
        fun newInstance(context: Context) = Config(context)
    }

    var userId: String
        get() = sharedPreferences?.getString(USER_ID, "")!!
        set(value) = sharedPreferences?.edit()?.putString(USER_ID, value)?.apply()!!

    var userRole: String
        get() = sharedPreferences?.getString(USER_ROLE, "")!!
        set(value) = sharedPreferences?.edit()?.putString(USER_ROLE, value)?.apply()!!

    var userName: String
        get() = sharedPreferences?.getString(USER_NAME, "")!!
        set(value) = sharedPreferences?.edit()?.putString(USER_NAME, value)?.apply()!!

    var userEmail: String
        get() = sharedPreferences?.getString(USER_EMAIL, "")!!
        set(value) = sharedPreferences?.edit()?.putString(USER_EMAIL, value)?.apply()!!

    var isLoggedIn: Boolean
        get() = sharedPreferences?.getBoolean(IS_LOGGED_IN, false)!!
        set(value){
            Log.d("DEBUGGING", "kur isLoggedIn set to $value")
            sharedPreferences?.edit()?.putBoolean(IS_LOGGED_IN, value)?.apply()!!
        }

    fun clear() {
        sharedPreferences?.edit()?.clear()?.apply()
    }

    fun logout() {
        isLoggedIn = false
        clear()
    }
}
