package com.example.app_comp

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

val mAuth: FirebaseAuth get() = FirebaseAuth.getInstance()

val storage: FirebaseStorage get() = FirebaseStorage.getInstance()

val db: FirebaseFirestore get() = FirebaseFirestore.getInstance()

fun AppCompatActivity.replaceFragment(fragment: Fragment) {
    val backStateName: String = fragment.javaClass.name
    val frameLayout: FrameLayout = findViewById(R.id.frame_layout)

    val popFragment: Boolean = supportFragmentManager.popBackStackImmediate(backStateName, 0)
    if (!popFragment) {
        val beginTrans = supportFragmentManager.beginTransaction()
        beginTrans.setCustomAnimations(
            /* enter = */ R.anim.slide_in,
            /* exit = */ R.anim.fade_out,
            /* popEnter = */ R.anim.fade_in,
            /* popExit = */ R.anim.slide_out
        )
        beginTrans.replace(frameLayout.id, fragment)
        beginTrans.addToBackStack(backStateName)
        beginTrans.commit()
    }
}

fun FragmentActivity.replaceFragment(fragment: Fragment) {
    val backStateName: String = fragment.javaClass.name
    val  frameLayout: FrameLayout = findViewById(R.id.frame_layout)

    val popFragment: Boolean = supportFragmentManager.popBackStackImmediate(backStateName, 0)
    if (!popFragment) {
        val beginTrans = supportFragmentManager.beginTransaction()
        beginTrans.setCustomAnimations(
            /* enter = */ R.anim.slide_in,
            /* exit = */ R.anim.fade_out,
            /* popEnter = */ R.anim.fade_in,
            /* popExit = */ R.anim.slide_out
        )
        beginTrans.replace(frameLayout.id, fragment)
        beginTrans.addToBackStack(backStateName)
        beginTrans.commit()
    }
}

fun Fragment.replaceFragment(fragment: Fragment) {
    requireActivity().replaceFragment(fragment)
}

fun Context.showToast(string: String) {
    Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
}
fun Context.showToast(stringId: Int) {
    Toast.makeText(this, stringId, Toast.LENGTH_SHORT).show()
}
fun Fragment.showToast(string: String) {
    Toast.makeText(requireContext(), string, Toast.LENGTH_SHORT).show()
}
fun Fragment.showToast(stringId: Int) {
    Toast.makeText(context, stringId, Toast.LENGTH_SHORT).show()
}

fun Context.hideKeyboard(view: View) {
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun isValidEmail(email: String): Boolean =
    email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()


// AppCompatActivity -> FragmentActivity -> Activity -> ContextThemeWrapper -> ContextWrapper - sub class of Context
// a new instance is created with the context of the whole app
// Activities use this config
// get is called when
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


