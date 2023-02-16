package com.example.app_comp

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_comp.databinding.FragmentPostComplaintBinding
import com.example.app_comp.databinding.FragmentViewComplaintBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import androidx.fragment.app.viewModels

class ViewComplaintFragment : Fragment() {

    private lateinit var binding: FragmentViewComplaintBinding
    private lateinit var complaintAdapter: ComplaintAdapter
    private val  viewModel: UserViewModel by viewModels()
    private var complaints = emptyList<Complaint>()
    private var buttonListener: ButtonVisibilityListener? = null

    interface ButtonVisibilityListener {
        fun setButtonVisible()
        fun setButtonInvisible()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // Set the button visibility listener based on the parent activity
        if (context is ButtonVisibilityListener) {
            buttonListener = context
        } else {
            throw RuntimeException("$context must implement ButtonVisibilityListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
        } catch (e: Exception) {
            Log.e(DEBUGGING, "vcf: Error in onCreate: ${e.message}")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        try {
            // Inflate the layout for this fragment
            binding = FragmentViewComplaintBinding.inflate(inflater, container, false)
            //(activity as UserActivity).setButtonInvisible()

            // Set up the RecyclerView

            complaintAdapter = ComplaintAdapter(complaints)
            binding.recyclerViewComplaints.adapter = complaintAdapter
            binding.recyclerViewComplaints.layoutManager = LinearLayoutManager(context)

            // Call the API to retrieve the complaint data
            bindViewModel(viewModel)

            return binding.root
        }catch (e: Exception){
            Log.e(DEBUGGING, "vcf: Error in onCreateView: ${e.message}")
            return null
        }
    }

    private fun bindViewModel(viewModel: UserViewModel) {
        lifecycleScope.launch {
            viewModel.getComplaints().collect { result ->
                //val result = viewModel.getComplaints().first()
                when (result) {
                    is Result.Success -> {
                        Log.d(DEBUGGING, "Complaints fetched successfully")
                        // Do something with the fetched complaints
                        val filteredComplaints = result.data.filter { it.userId == config.userId }
                        //val complaintAdapter = ComplaintAdapter(result.data)
                        val complaintAdapter = ComplaintAdapter(filteredComplaints)
                        binding.recyclerViewComplaints.adapter = complaintAdapter
                    }
                    is Result.Error -> {
                        Log.d(DEBUGGING, "Error fetching complaints: ${result.exception}")
                        showToast("Error fetching complaints")
                    }
                    is Result.Failed -> {
                        Log.d(DEBUGGING, "Error fetching complaints: ${result.error} message:${result.message}")
                        showToast("Failed fetching complaints")
                    }
                    is Result.Loading -> {
                        Log.d(DEBUGGING, "Loading fetching complaints: ${result.isLoading}")
                        showProgress()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireFragmentManager().popBackStack()
            }
        })
    }

    override fun onPause() {
        super.onPause()
        //(activity as UserActivity).setButtonVisible()
        //(activity as AdminActivity).setButtonVisible()
        buttonListener?.setButtonVisible()
    }

}