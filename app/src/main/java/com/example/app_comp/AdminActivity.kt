package com.example.app_comp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.app_comp.databinding.ActivityAdminBinding
import com.example.app_comp.login.LoginActivity
import com.example.app_comp.utils.config

class AdminActivity : AppCompatActivity(){

    private lateinit var binding: ActivityAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}