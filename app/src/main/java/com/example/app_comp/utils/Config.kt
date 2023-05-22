package com.example.app_comp.utils

import android.content.Context

class Config(context: Context) {
    val prefs = context.getSharedPrefs()
    companion object {
        fun newInstance(context: Context) = Config(context)
    }

    var userId: String
        get() = prefs?.getString(USER_ID, "")!!
        set(value) = prefs?.edit()?.putString(USER_ID, value)?.apply()!!

    var userRole: String
        get() = prefs?.getString(USER_ROLE, "")!!
        set(value) = prefs?.edit()?.putString(USER_ROLE, value)?.apply()!!

    var userName: String
        get() = prefs?.getString(USER_NAME, "")!!
        set(value) = prefs?.edit()?.putString(USER_NAME, value)?.apply()!!

    var userEmail: String
        get() = prefs?.getString(USER_EMAIL, "")!!
        set(value) = prefs?.edit()?.putString(USER_EMAIL, value)?.apply()!!

    var isLoggedIn: Boolean
        get() = prefs?.getBoolean(IS_LOGGED_IN, false)!!
        set(value){
            prefs?.edit()?.putBoolean(IS_LOGGED_IN, value)?.apply()!!
        }

    private fun clear() {
        prefs?.edit()?.clear()?.apply()
    }

    fun logout() {
        isLoggedIn = false
        clear()
    }
}
