package com.example.app_comp

import android.content.Context

class Config(context: Context) {
    private val prefs = context.getSharedPrefs()
    companion object {
        fun newInstance(context: Context) = Config(context)
    }

    var userId: String
        get() = prefs.getString(USER_ID, "")!!
        set(userId) = prefs.edit().putString(USER_ID, userId).apply()

    var userRole: String
        get() = prefs.getString(USER_ROLE, "")!!
        set(userRole) = prefs.edit().putString(USER_ROLE, userRole).apply()

    var userName: String
        get() = prefs.getString(USER_NAME, "")!!
        set(userRole) = prefs.edit().putString(USER_NAME, userRole).apply()

    var userEmail: String
        get() = prefs.getString(USER_EMAIL, "")!!
        set(userRole) = prefs.edit().putString(USER_EMAIL, userRole).apply()

    var isLoggedIn: Boolean
        get() = prefs.getBoolean(IS_LOGGED_IN, false)
        set(isLoggedIn) = prefs.edit().putBoolean(IS_LOGGED_IN, isLoggedIn).apply()


}