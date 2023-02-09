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
//        Log.d(DEBUGGING, "swag" + config.isLoggedIn.toString())
        if (config.isLoggedIn) {
//            Log.d(DEBUGGING, "hui")
            if(config.userRole == USER_ROLE){
                startActivity(Intent(this, UserActivity::class.java))
                finish()
            }
            if(config.userRole == ADMIN_ROLE){
                startActivity(Intent(this, AdminActivity::class.java))
                finish()
            }
        } else {
            replaceFragment(LoginFragment())
        }
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        config.logout()
//    }
}