package com.example.app_comp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_comp.data.Complaint
import com.example.app_comp.databinding.ActivityAdminBinding
import com.example.app_comp.login.LoginActivity
import com.example.app_comp.utils.config
import com.example.app_comp.utils.Result
import com.example.app_comp.utils.showToast
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class AdminActivity : AppCompatActivity(), ComplaintAdapter.OnItemClickListener {

    private lateinit var binding: ActivityAdminBinding
    private val viewModel: AdminViewModel by viewModels()
    private lateinit var complaintAdapter: ComplaintAdapter
    private var complaints = emptyList<Complaint>()
    private var currentFragment: Fragment? = null
    private lateinit var complaintDetailsFragmentBBD: AlertDialog
    private lateinit var homeScreenBBD: AlertDialog
    private lateinit var logoutDialog: AlertDialog



    override fun onItemClick(complaint: Complaint) {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Choose Action")
            .setMessage("Select an action for the complaint:")
            .setPositiveButton("View Details") { _, _ ->
                // Open ComplaintDetailsFragment
                openComplaintDetailsFragment(complaint)
            }
            .setNegativeButton("Update Status") { _, _ ->
                // Show dialog for updating the status
                showStatusUpdateDialog(complaint)
            }
            .create()

        dialog.show()
    }


    private fun openComplaintDetailsFragment(complaint: Complaint) {
        val fragment = ComplaintDetailsFragment.newInstance(complaint.id)
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun showStatusUpdateDialog(complaint: Complaint) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_status_update, null)
        val editTextStatus = dialogView.findViewById<EditText>(R.id.editTextStatus)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Update Status")
            .setView(dialogView)
            .setPositiveButton("Update") { _, _ ->
                val newStatus = editTextStatus.text.toString()
                updateComplaintStatus(complaint.id, newStatus)
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }

    private fun updateComplaintStatus(complaintId: String, newStatus: String) {
        viewModel.updateComplaintStatus(complaintId, newStatus)
            .onEach { result ->
                when (result) {
                    is Result.Success -> {
                        showToast("Status updated successfully")
                        // Refresh the complaints list if needed
                        viewModel.getAllComplaints()
                    }
                    is Result.Error -> {
                        showToast("Failed to update status: ${result.exception}")
                    }
                    else -> {}
                }
            }
            .launchIn(lifecycleScope)
    }


    override fun onBackPressed() {
        showToast("back press from ${currentFragment.toString()}")
        when (currentFragment) {
            is ImageFullscreenFragment -> {
                super.onBackPressed()
                currentFragment = PostComplaintFragment()
            }
            is ComplaintDetailsFragment -> {
                binding.rvComplaints.visibility = View.INVISIBLE
                complaintDetailsFragmentBBD.show()
            }
            null -> {
                super.onBackPressed()
                endCurrentFragment()
                homeScreenBBD.show()
                endAllFragments()
            }
        }

    }

    private fun endAllFragments() {
        supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        currentFragment = null
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)

        complaintDetailsFragmentBBD = AlertDialog.Builder(this)
            .setTitle("Reviewing complaint")
            .setMessage("Exit to home screen?")
            .setIcon(R.drawable.ic_question_foreground)
            .setPositiveButton("Yes"){ _,_ ->
                showToast("Home screen")
                supportFragmentManager.beginTransaction().remove(currentFragment!!).commit()
//                super.onBackPressed()
            }
            .setNegativeButton("No"){ _,_ ->
                showToast("Continuing work")
            }.create()

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

        complaintAdapter = ComplaintAdapter(complaints, this)
        complaintAdapter.setOnItemClickListener(this)
        binding.rvComplaints.adapter = complaintAdapter
        binding.rvComplaints.layoutManager = LinearLayoutManager(this)

        binding.toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.action_logout -> {
                    logoutDialog.show()
                    true
                }
                else -> false
            }
        }

        viewModel.getAllComplaints()
            .onEach { result ->
                when (result) {
                    is Result.Success -> {
                        val allComplaints = result.data
                        complaintAdapter.updateComplaints(allComplaints)
                        complaintAdapter.notifyDataSetChanged()
                    }
                    is Result.Error -> {
                        showToast("Failed to retrieve complaints: ${result.exception}")
                    }
                    else -> {}
                }
            }
            .launchIn(lifecycleScope)

        setContentView(binding.root)
    }

}