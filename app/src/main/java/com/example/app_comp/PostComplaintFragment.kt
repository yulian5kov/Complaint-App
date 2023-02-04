package com.example.app_comp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.app_comp.databinding.FragmentPostComplaintBinding
import androidx.navigation.fragment.findNavController


class PostComplaintFragment : Fragment() {
    private lateinit var binding: FragmentPostComplaintBinding
    private val viewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostComplaintBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.postComplaintFragment -> {
                    requireActivity().onBackPressedDispatcher.addCallback(this) {
                        navController.navigate(R.id.action_postComplaintFragment_to_userActivity)
                    }
                }
                else -> {
                    requireActivity().onBackPressedDispatcher.removeCallback(this)
                }
            }
        }

        binding.btnAddImage.setOnClickListener {
            // Add image code here
        }
        binding.btnPostComplaint.setOnClickListener {
            val complaintText = binding.etComplaintText.text.toString()
            val complaint = Complaint(complaintText)
            viewModel.postComplaint(complaint)
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            val intent = Intent(requireContext(), UserActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

    }

}
