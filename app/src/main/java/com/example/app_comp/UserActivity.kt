package com.example.app_comp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.app_comp.databinding.ActivityUserBinding

class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
    }
}