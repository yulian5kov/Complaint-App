package com.example.app_comp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.app_comp.databinding.ActivityAdminBinding
import com.example.app_comp.login.LoginActivity

class AdminActivity : AppCompatActivity(), ViewComplaintFragment.ButtonVisibilityListener {

    private lateinit var binding: ActivityAdminBinding

    override fun setButtonInvisible() {
        binding.btnViewComplaints.visibility = View.GONE
        binding.btnLogout.visibility = View.GONE
    }

    override fun setButtonVisible() {
        binding.btnViewComplaints.visibility = View.VISIBLE
        binding.btnLogout.visibility = View.VISIBLE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogout.setOnClickListener {
            config.logout()
            val intent = Intent(this@AdminActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnViewComplaints.setOnClickListener {
            binding.btnLogout.visibility = View.GONE
            replaceFragment(ViewComplaintFragment())
        }
    }

    override fun onBackPressed() {
        Log.d(DEBUGGING, "onbackpressed entered in adminactivity")
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
            finish() // add this line to close the application
        }
    }

    //This will remove the fragment from the fragment manager and free up any resources associated with it.
    override fun onDestroy() {
        super.onDestroy()
        if (!isFinishing) {
            supportFragmentManager.beginTransaction().remove(ViewComplaintFragment()).commit()
        }
    }

}