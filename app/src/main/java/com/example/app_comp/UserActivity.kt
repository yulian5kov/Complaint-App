package com.example.app_comp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_comp.data.Complaint
import com.example.app_comp.databinding.ActivityUserBinding
import com.example.app_comp.login.LoginActivity
import com.example.app_comp.utils.config
import com.example.app_comp.utils.mAuth
import com.example.app_comp.utils.showToast
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import com.example.app_comp.utils.Result


class UserActivity : AppCompatActivity(), ComplaintAdapter.OnItemClickListener {

    private lateinit var binding: ActivityUserBinding
    private lateinit var logoutDialog: AlertDialog
    private lateinit var homeScreenBBD: AlertDialog
    private lateinit var postComplaintFragmentBBD: AlertDialog
    private lateinit var complaintDetailsFragmentBBD: AlertDialog
    private val viewModel: UserViewModel by viewModels()
    private lateinit var complaintAdapter: ComplaintAdapter
    private var complaints = emptyList<Complaint>()
    private var currentFragment: Fragment? = null

    fun setCurrentFragment(fragment: Fragment?) {
        currentFragment = fragment
    }

    override fun onStart() {
        super.onStart()
        showToast("current fragment: " + currentFragment.toString())
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        logoutDialog = AlertDialog.Builder(this)
            .setTitle("Logging out")
            .setMessage("Do you wish to log out?")
            .setIcon(R.drawable.ic_question_foreground)
            .setPositiveButton("Yes"){ _,_ ->
                showToast("You've successfully logged out")
                config.logout()
                finish()
                startActivity(Intent(this, LoginActivity::class.java))
            }
            .setNegativeButton("No"){ _,_ ->
                showToast("Continuing work")
            }.create()

        homeScreenBBD = AlertDialog.Builder(this)
            .setTitle("Exiting app")
            .setMessage("Do you want to exit?")
            .setIcon(R.drawable.ic_question_foreground)
            .setPositiveButton("Yes"){ _,_ ->
                showToast("See you later")
                finishAffinity()
                super.onBackPressed()
            }
            .setNegativeButton("No"){ _,_ ->
                showToast("Continuing work")
            }.create()

        complaintDetailsFragmentBBD = AlertDialog.Builder(this)
            .setTitle("Reviewing complaint")
            .setMessage("Exit to home screen?")
            .setIcon(R.drawable.ic_question_foreground)
            .setPositiveButton("Yes"){ _,_ ->
                showToast("Home screen")
                binding.rvComplaints.visibility = VISIBLE
                supportFragmentManager.beginTransaction().remove(currentFragment!!).commit()
                binding.btnPost.visibility = VISIBLE
//                super.onBackPressed()
            }
            .setNegativeButton("No"){ _,_ ->
                showToast("Continuing work")
            }.create()

        postComplaintFragmentBBD = AlertDialog.Builder(this)
            .setTitle("Changes will be lost")
            .setMessage("Exit to home screen?")
            .setIcon(R.drawable.ic_question_foreground)
            .setPositiveButton("Yes"){ _,_ ->
                showToast("Home screen")
                binding.rvComplaints.visibility = VISIBLE
                binding.btnPost.visibility = VISIBLE
                endCurrentFragment()
//                super.onBackPressed()
            }
            .setNegativeButton("No"){ _,_ ->
                binding.btnPost.visibility = INVISIBLE
                binding.rvComplaints.visibility = INVISIBLE
                showToast("Continuing work")
            }.create()

        complaintAdapter = ComplaintAdapter(complaints, this)
        complaintAdapter.setOnItemClickListener(this)
        binding.rvComplaints.adapter = complaintAdapter
        binding.rvComplaints.layoutManager = LinearLayoutManager(this)

        val userId = mAuth.currentUser?.uid

        if (userId != null) {
            viewModel.getComplaintsByUser(userId)
                .onEach { result ->
                    when (result) {
                        is Result.Success -> {
                            val complaints = result.data
                            complaintAdapter.updateComplaints(complaints)
                            complaintAdapter.notifyDataSetChanged()
                        }
                        is Result.Error -> {
                            showToast("Failed to retrieve complaints: ${result.exception}")
                        }

                        else -> {}
                    }
                }
                .launchIn(lifecycleScope)
        } else {
            showToast("User not logged in")
        }

        binding.toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.action_logout -> {
                    logoutDialog.show()
                    true
                }
                else -> false
            }
        }

        binding.btnPost.setOnClickListener {
            binding.btnPost.visibility = INVISIBLE
            binding.rvComplaints.visibility = INVISIBLE
            supportFragmentManager.beginTransaction().apply{
                replace(R.id.frame_layout, PostComplaintFragment())
                commit()
            }
        }
    }

    override fun onBackPressed() {
        showToast("back press from ${currentFragment.toString()}")
//        if(binding.rvComplaints.visibility == VISIBLE && binding.btnPost.visibility == VISIBLE){
//            super.onBackPressed()
//        }
        when (currentFragment) {
            is PostComplaintFragment -> {
//                binding.rvComplaints.visibility = View.INVISIBLE
//                binding.btnPost.visibility = View.INVISIBLE
                postComplaintFragmentBBD.show()
            }
            is ImageFullscreenFragment -> {
                super.onBackPressed()
                currentFragment = PostComplaintFragment()
            }
            is ComplaintDetailsFragment -> {
                binding.rvComplaints.visibility = View.INVISIBLE
                binding.btnPost.visibility = View.INVISIBLE
                complaintDetailsFragmentBBD.show()
            }
            is MapFragment -> {
                super.onBackPressed()
                currentFragment = PostComplaintFragment()
            }
            null -> {
                super.onBackPressed()
                endCurrentFragment()
                binding.rvComplaints.visibility = View.VISIBLE
                binding.btnPost.visibility = View.VISIBLE
                homeScreenBBD.show()
                endAllFragments()
            }
        }

    }

    private fun endCurrentFragment() {
        Log.d("curfrag", currentFragment.toString())
        currentFragment?.let { fragment ->
            if (fragment is ComplaintDetailsFragment || fragment is PostComplaintFragment) {
                fragment.view?.visibility = View.INVISIBLE
            }
        }

        if (currentFragment != null) {
            supportFragmentManager.beginTransaction()
                .remove(currentFragment!!)
                .commit()
            currentFragment = null
        }
    }

    private fun endAllFragments() {
        supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        currentFragment = null
    }

    override fun onItemClick(complaint: Complaint) {
        binding.btnPost.visibility = INVISIBLE
        binding.rvComplaints.visibility = INVISIBLE
        val complaintId = complaint.id
        val fragment = ComplaintDetailsFragment.newInstance(complaintId)
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .addToBackStack(null)
            .commit()
    }

}