package com.example.app_comp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.app_comp.databinding.ActivityUserBinding
import com.example.app_comp.login.LoginActivity


class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding
    //val btnAddComplaint: Button? = findViewById<Button>(R.id.btn_add_complaint)
    private lateinit var btnAddComplaint: Button

    fun setButtonInvisible() {
        btnAddComplaint.visibility = View.GONE
    }

    fun setButtonVisible() {
        btnAddComplaint.visibility = View.VISIBLE
    }

    fun getBinding(): ActivityUserBinding {
        return binding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        //
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btnAddComplaint = findViewById(R.id.btn_add_complaint)

        binding.btnLogout.setOnClickListener {
            config.logout()
            val intent = Intent(this@UserActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnAddComplaint.setOnClickListener {
            binding.btnLogout.visibility = View.GONE
            replaceFragment(PostComplaintFragment())
        }
    }

    override fun onBackPressed() {
        Log.d(DEBUGGING, "putka")
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }


    //This will remove the fragment from the fragment manager and free up any resources associated with it.
    override fun onDestroy() {
        super.onDestroy()
        if (!isFinishing) {
            supportFragmentManager.beginTransaction().remove(PostComplaintFragment()).commit()
        }
    }



}