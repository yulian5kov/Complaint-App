package com.example.app_comp.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesManager(private val context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("com.example.app.prefs", Context.MODE_PRIVATE)

    var location: String
        get() = sharedPreferences.getString("LOCATION", "") ?: ""
        set(value) = sharedPreferences.edit().putString("LOCATION", value).apply()
}