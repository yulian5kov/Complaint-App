package com.example.app_comp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.app_comp.databinding.ActivityUserBinding
import com.example.app_comp.login.LoginActivity
import com.example.app_comp.login.LoginFragment


class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogout.setOnClickListener {
            config.isLoggedIn = false
            config.userId = ""
            config.userName = ""
            config.userEmail = ""
            config.userRole = ""
            //exit here
            val intent = Intent(this@UserActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnAddComplaint.setOnClickListener {
            binding.btnLogout.visibility = View.GONE
            val navController = findNavController(R.id.nav_host_fragment)
            navController.navigate(R.id.postComplaintFragment)
            //replaceFragment(PostComplaintFragment())
        }
    }

    override fun onBackPressed() {
        finish()
    }
}