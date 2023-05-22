package com.example.app_comp.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.example.app_comp.*
import com.example.app_comp.databinding.ActivityLoginBinding
import com.example.app_comp.utils.ADMIN_ROLE
import com.example.app_comp.utils.USER_ROLE
import com.example.app_comp.utils.config

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
            supportFragmentManager.beginTransaction().apply{
                replace(R.id.frame_layout, LoginFragment())
                commit()
            }
        }
    }


}