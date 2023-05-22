package com.example.app_comp

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_comp.data.Complaint
import com.example.app_comp.utils.Result
import com.example.app_comp.databinding.FragmentComplaintDetailsBinding
import com.example.app_comp.utils.showToast
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ComplaintDetailsFragment : Fragment() {

    private lateinit var binding: FragmentComplaintDetailsBinding
    private val viewModel: UserViewModel by viewModels()
//    private lateinit var imageAdapter: ImageAdapter

    override fun onPause() {
        (activity as? UserActivity)?.setCurrentFragment(null)
        super.onPause()
    }

    override fun onResume() {
        (activity as? UserActivity)?.setCurrentFragment(this)
        super.onResume()
    }

    companion object {
        private const val ARG_COMPLAINT_ID = "complaint_id"

        fun newInstance(complaintId: String): ComplaintDetailsFragment {
            val fragment = ComplaintDetailsFragment()
            val args = Bundle()
            args.putString(ARG_COMPLAINT_ID, complaintId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? UserActivity)?.setCurrentFragment(this)

        requireActivity().window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)

        val complaintId = arguments?.getString(ARG_COMPLAINT_ID)
        if (complaintId != null) {
            // Load the complaint information using the complaintId from Firebase
            // Example: viewModel.getComplaintById(complaintId)

            viewModel.getComplaintById(complaintId)
                .onEach { result ->
                    when (result) {
                        is Result.Success -> {
                            val complaint = result.data
                            // Display the complaint details
                            displayComplaintDetails(complaint)
                            Log.d("swag", complaint.images.toString())
                        }
                        is Result.Error -> {
                            showToast("Failed to retrieve complaint details: ${result.exception}")
                        }

                        else -> {}
                    }
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)


        }
    }

    private fun displayComplaintDetails(complaint: Complaint) {
        // Update the UI with the complaint details
        binding.tvTitle.text = complaint.title
        binding.tvLocation.text = complaint.location
        binding.tvDesc.text = complaint.description
        Log.d("pichka", complaint.images.toString())
        // Convert the list of image URLs to a list of Uri objects
        val imageUris = complaint.images.map { Uri.parse(it) }

         val adapter = ImageAdapter(requireContext(), imageUris)
         binding.rvImages.adapter = adapter
        binding.rvImages.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentComplaintDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }


}