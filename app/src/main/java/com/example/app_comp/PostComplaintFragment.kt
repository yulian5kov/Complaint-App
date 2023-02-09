package com.example.app_comp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
//import androidx.fragment.app.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.app_comp.databinding.FragmentPostComplaintBinding
import kotlin.system.exitProcess
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentActivity


class PostComplaintFragment : Fragment() {
    private lateinit var binding: FragmentPostComplaintBinding
    private val viewModel: UserViewModel by viewModels()
    private lateinit var backCallback: OnBackPressedCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostComplaintBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Step 2: Attach the back button behavior to this fragment
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            // Step 3: Handle the back button press
            // In this example, we just pop the fragment off the back stack,
            // but you could do anything you want here
            activity?.supportFragmentManager?.popBackStack()
        }
    }


//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        binding.btnAddImage.setOnClickListener {
//            // Add image code here
//        }
//        binding.btnPostComplaint.setOnClickListener {
//            val complaintText = binding.etComplaintText.text.toString()
//            val complaint = Complaint(complaintText)
//            viewModel.postComplaint(complaint)
//        }
//
//        backCallback = object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                requireActivity().onBackPressed()
//            }
//        }
//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, backCallback)
//    }

//    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
//
//        if (item.itemId == android.R.id.home) {
//            requireFragmentManager().popBackStack()
//            return true
//        }
//        return super.onOptionsItemSelected(item)
//    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        requireActivity().onBackPressedDispatcher.removeCallback(backCallback)
//    }
//    fun onSupportNavigateUp(): Boolean {
//        requireActivity().onBackPressed()
//        return true
//    }
}

