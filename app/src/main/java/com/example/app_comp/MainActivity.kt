package com.example.app_comp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
//import androidx.fragment.app.activityViewModels

class MainActivity : AppCompatActivity() {

    //val viewModel : LoginViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}