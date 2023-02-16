package com.example.app_comp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.app_comp.databinding.ActivityUserBinding
import com.example.app_comp.login.LoginActivity


class UserActivity : AppCompatActivity(), ViewComplaintFragment.ButtonVisibilityListener  {

    private lateinit var binding: ActivityUserBinding
    private lateinit var btnAddComplaint: Button
    private lateinit var btnViewComplaint: Button

    override fun setButtonInvisible() {
        btnAddComplaint.visibility = View.GONE
        btnViewComplaint.visibility = View.GONE
        binding.btnLogout.visibility = View.GONE
    }

    override fun setButtonVisible() {
        btnAddComplaint.visibility = View.VISIBLE
        btnViewComplaint.visibility = View.VISIBLE
        binding.btnLogout.visibility = View.VISIBLE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        //
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btnAddComplaint = findViewById(R.id.btn_add_complaint)
        btnViewComplaint = findViewById(R.id.btn_view_complaints)

        binding.btnLogout.setOnClickListener {
            config.logout()
            val intent = Intent(this@UserActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnAddComplaint.setOnClickListener {
            replaceFragment(PostComplaintFragment())
        }

        binding.btnViewComplaints.setOnClickListener {
            binding.btnLogout.visibility = View.GONE
            replaceFragment(ViewComplaintFragment())
        }
    }

    override fun onBackPressed() {
        Log.d(DEBUGGING, "onbackpressed entered in useractivity")
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
            supportFragmentManager.beginTransaction().remove(ViewComplaintFragment()).commit()
        }
    }



}