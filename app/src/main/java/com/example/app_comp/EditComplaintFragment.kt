package com.example.app_comp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_comp.databinding.FragmentEditComplaintBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class EditComplaintFragment : Fragment() {

    companion object {
        private const val ARG_COMPLAINT_ID = "complaint_id"

        fun newInstance(complaintId: String): EditComplaintFragment {
            val args = Bundle()
            args.putString(ARG_COMPLAINT_ID, complaintId)
            val fragment = EditComplaintFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var binding: FragmentEditComplaintBinding
    private val viewModel: UserViewModel by viewModels()
    private lateinit var complaint: Complaint
    private lateinit var imageAdapter: ImageAdapter
    private var isEditMode = false

    //val statusOptions = resources.getStringArray(R.array.complaint_status)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val complaintId = arguments?.getString(ARG_COMPLAINT_ID) ?: return
        lifecycleScope.launchWhenResumed {
            viewModel.getComplaintById(complaintId).collect { result ->
                when (result) {
                    is Result.Success -> {
                        hideProgress()
                        complaint = result.data

                        // Display the complaint information
                        binding.editTitle.setText(complaint.title)
                        binding.editDescription.setText(complaint.description)
                        binding.editLocation.setText(complaint.location)
                        binding.textDate.text = SimpleDateFormat("MMM d, yyyy h:mm a", Locale.getDefault()).format(complaint.date)
                        //binding.spinnerStatus.setSelection(statusOptions.indexOf(complaint.status))
                        binding.editStatus.setText(complaint.status)

                        // Set up the image recycler view
                        imageAdapter = ImageAdapter(requireContext())
                        binding.recyclerViewImages.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                        binding.recyclerViewImages.adapter = imageAdapter

                        // Check if the current user is an admin
                        //val currentUser = viewModel.getCurrentUser()
                        if (config.userRole == ADMIN_ROLE) {
                            // Admin can only edit the status
                            binding.editTitle.isEnabled = false
                            binding.editDescription.isEnabled = false
                            binding.editLocation.isEnabled = false
                            binding.recyclerViewImages.isEnabled = false
                            isEditMode = true
                        } else {
                            // User can edit everything except the date and status

                            binding.textDate.isEnabled = false
                            binding.editStatus.isEnabled = false
                            isEditMode = true
                        }
                    }
                    is Result.Error -> {
                        showToast("Failed to load complaint: ${result.exception}")
                        requireFragmentManager().popBackStack()
                    }
                    is Result.Failed -> {
                        showToast("Failed to update complaint: ${result.error} + ${result.message}")
                    }
                    is Result.Loading -> {
                        showProgress()
                    }
                }
            }
        }


        binding.buttonEdit.setOnClickListener {
            if (!isEditMode) {
                // Enable edit mode
                binding.editTitle.isEnabled = true
                binding.editDescription.isEnabled = true
                binding.editLocation.isEnabled = true
                binding.recyclerViewImages.isEnabled = true
                isEditMode = true
            } else {
                // Save changes to the complaint
                complaint.title = binding.editTitle.text.toString()
                complaint.description = binding.editDescription.text.toString()
                complaint.location = binding.editLocation.text.toString()
                complaint.images = imageAdapter.getImages().map { it.toString() }
                lifecycleScope.launch {
                    try {
                        val result = viewModel.updateComplaint(complaint)
                        showToast("Complaint updated")
                        // Disable edit mode
                        binding.editTitle.isEnabled = false
                        binding.editDescription.isEnabled = false
                        binding.editLocation.isEnabled = false
                        binding.recyclerViewImages.isEnabled = false
                        isEditMode = false
                    } catch (e: Exception) {
                        showToast("Failed to update complaint: ${e.message}")
                    }
                }

            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditComplaintBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (activity as UserActivity).setButtonInvisible()
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireFragmentManager().popBackStack()
            }
        })
    }

    override fun onPause() {
        super.onPause()
        (activity as UserActivity).setButtonVisible()
    }

}