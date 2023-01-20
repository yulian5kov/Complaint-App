package com.example.app_comp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.example.app_comp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_login)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

    }

    private fun init(){
        replaceFragment(LoginFragment())
    }

    fun /*AppCompatActivity.*/replaceFragment(fragment: Fragment) {
        val backStateName: String = fragment.javaClass.name
        //val frameLayout: FrameLayout = findViewById(R.id.frame_layout)
        val frameLayout: FrameLayout = binding.frameLayout

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

}