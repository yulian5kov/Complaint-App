package com.example.app_comp

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
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

val Context.config: Config get() = Config.newInstance(applicationContext)

val Fragment.config: Config get() = Config.newInstance(requireActivity().applicationContext)

val mAuth: FirebaseAuth get() = FirebaseAuth.getInstance()

val storage: FirebaseStorage get() = FirebaseStorage.getInstance()

val db: FirebaseFirestore get() = FirebaseFirestore.getInstance()

fun Context.getSharedPrefs(): SharedPreferences = getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)

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

