package com.example.app_comp.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Patterns
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

val mAuth: FirebaseAuth get() = FirebaseAuth.getInstance()
val storage: FirebaseStorage get() = FirebaseStorage.getInstance()
val db: FirebaseFirestore get() = FirebaseFirestore.getInstance()

fun Fragment.showToast(string: String) {
    Toast.makeText(requireContext(), string, Toast.LENGTH_SHORT).show()
}
fun FragmentActivity.showToast(string: String) {
    Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
}

fun isValidEmail(email: String): Boolean =
    email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()

// AppCompatActivity -> FragmentActivity -> Activity -> ContextThemeWrapper -> ContextWrapper - sub class of Context
// Activities use this config
val Context.config: Config get() = Config.newInstance(applicationContext)

// Fragments use this config
// requireActivity is used to get the context of the activity that started the given fragment
// activity?.applicationContext may return null
// requireActivity() returns IllegalStateException if null
val Fragment.config: Config get() = Config.newInstance(requireActivity().applicationContext)

// 1st arg - PREFS_KEY identifies the preferences file to be used
// 2nd arg - integer constant that specifies the mode for accessing the preferences file
// MODE_PRIVATE = only this application can read or modify the preferences file.
fun Context.getSharedPrefs(): SharedPreferences = getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)


