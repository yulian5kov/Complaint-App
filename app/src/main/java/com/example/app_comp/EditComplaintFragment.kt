package com.example.app_comp

import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.app_comp.databinding.FragmentEditComplaintBinding
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.launch

class EditComplaintFragment : Fragment() {

    private lateinit var binding: FragmentEditComplaintBinding
    private val viewModel: UserViewModel by viewModels()
    private lateinit var complaintId: String
    private var images: MutableList<Uri> = mutableListOf()

    private fun MutableList<Uri>.toStringList(): List<String> {
        return this.map { it.toString() }
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        try {
//            super.onCreate(savedInstanceState)
//            arguments?.let {
//                complaintId = it.getString(ARG_COMPLAINT_ID) ?: ""
//            }
//        } catch (e: Exception) {
//            Log.e(DEBUGGING, "Error in onCreate: ${e.message}")
//        }
//    }
//
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        try {
//            // Inflate the layout for this fragment
//            binding = FragmentEditComplaintBinding.inflate(inflater, container, false)
//
//            (activity as UserActivity).setButtonInvisible()
//
//            return binding.root
//        } catch (e: Exception) {
//            Log.e(DEBUGGING, "Error in onCreateView: ${e.message}")
//            return null
//        }
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        viewModel.getComplaintById(complaintId).observe(viewLifecycleOwner) { complaint ->
//            if (complaint != null) {
//                binding.etTitle.setText(complaint.title)
//                binding.etDescription.setText(complaint.description)
//                binding.tvLocation.setText(complaint.location)
//
//                if (complaint.images.isNotEmpty()) {
//                    images.addAll(complaint.images.map { Uri.parse(it) })
//                }
//
//                binding.ivImagePreview.adapter = ImagePreviewAdapter(images)
//            }
//        }
//
//        binding.btnAddLocation.setOnClickListener {
//            if (ActivityCompat.checkSelfPermission(
//                    requireContext(),
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                    requireContext(),
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                ActivityCompat.requestPermissions(
//                    requireActivity(),
//                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                    REQUEST_LOCATION_PERMISSION
//                )
//            } else {
//                getLocation()
//            }
//        }
//
//        binding..setOnClickListener {
//            val intent = Intent().apply {
//                type = "image/*"
//                action = Intent.ACTION_GET_CONTENT
//                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//            }
//            startActivityForResult(
//                Intent.createChooser(intent, "Select Images"),
//                REQUEST_CODE_IMAGE_PICK
//            )
//        }
//
//        binding.buttonSaveComplaint.setOnClickListener{
//            Log.d(DEBUGGING, "Button click listener registered")
//            val updatedComplaint = Complaint(
//                id = complaintId,
//                title = binding.editComplaintTitle.text.toString(),
//                description = binding.editComplaintDescription.text.toString(),
//                images = images.toStringList(),
//                userId = mAuth.currentUser!!.uid,
//                location = binding.textViewLocation.text.toString()
//            )
//            lifecycleScope.launch {
//                when (val result = viewModel.updateComplaint(updatedComplaint).first()) {
//                    is Result.Success -> {
//                        Log.d(DEBUGGING, "Complaint updated successfully")
//                        showToast("Complaint updated")
//                        findNavController().navigateUp()
//                    }
//                    is Result.Error -> {
//                        Log.d(DEBUGGING, "Error updating complaint: ${result.exception}")
//                        showToast("Error updating complaint")
//                    }
//                    is Result.Failed -> {
//                        Log.d(DEBUGGING, "Failed updating complaint: ${result.error} message:${result.message}")
//                        showToast("Failed updating complaint")
//                    }
//                    is Result.Loading -> {
//                        Log.d(DEBUGGING, "Updating complaint...")
//                    }
//                }
//            }
//        }
//    }

}