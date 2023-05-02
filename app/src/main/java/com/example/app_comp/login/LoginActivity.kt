package com.example.app_comp.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.app_comp.*
import com.example.app_comp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {


    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }
    private fun init(){
        // val Context.config: Config get() = Config.newInstance(applicationContext) - in Extensions file
        // companion object { fun newInstance(context: Context) = Config(context) } - in Config class
        if (config.isLoggedIn) {
            if(config.userRole == USER_ROLE){
                startActivity(Intent(this, UserActivity::class.java))
                finish()
            }
            if(config.userRole == ADMIN_ROLE){
                startActivity(Intent(this, AdminActivity::class.java))
                finish()
            }
        } else {
//            replaceFragment(LoginFragment())
            supportFragmentManager.beginTransaction().apply{
                // starts LoginFragment into FrameLayout of which is inside of LoginActivity
                replace(R.id.frame_layout, LoginFragment())
                // adds the fragment to the backstack so it closes when back btn is pressed
                // null refers to a name that can be given to this transaction
//                addToBackStack(null)
                commit()
            }
        }
    }


}