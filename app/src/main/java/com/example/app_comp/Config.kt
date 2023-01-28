package com.example.app_comp

import android.content.Context
import android.content.SharedPreferences

class Config(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)

    var userId: String
        get() = sharedPreferences.getString("userId", "")!!
        set(value) = sharedPreferences.edit().putString("userId", value).apply()

    var userRole: String
        get() = sharedPreferences.getString("userRole", "")!!
        set(value) = sharedPreferences.edit().putString("userRole", value).apply()

    var userName: String
        get() = sharedPreferences.getString("userName", "")!!
        set(value) = sharedPreferences.edit().putString("userName", value).apply()

    var userEmail: String
        get() = sharedPreferences.getString("userEmail", "")!!
        set(value) = sharedPreferences.edit().putString("userEmail", value).apply()

    var isLoggedIn: Boolean
        get() = sharedPreferences.getBoolean("isLoggedIn", false)
        set(value) = sharedPreferences.edit().putBoolean("isLoggedIn", value).apply()

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}
