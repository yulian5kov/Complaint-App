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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddImage.setOnClickListener {
            // Add image code here
        }
        binding.btnPostComplaint.setOnClickListener {
            val complaintText = binding.etComplaintText.text.toString()
            val complaint = Complaint(complaintText)
            viewModel.postComplaint(complaint)
        }

        backCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.d(DEBUGGING, "mazalqk")
                requireActivity().onBackPressed()
            }
        }
        //requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, backCallback)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().supportFragmentManager.popBackStack()
        }

    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            requireFragmentManager().popBackStack()
            //activity?.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //(activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            //requireActivity().onBackPressed()
            activity?.onBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //backCallback.remove()
        requireActivity().onBackPressedDispatcher.removeCallback(viewLifecycleOwner)
    }

    fun returnToUserActivity() {
//        supportFragmentManager.findFragmentByTag(PostComplaintFragment.TAG)?.let { fragment ->
//            supportFragmentManager.beginTransaction().remove(fragment).commit()
//        }
        fragmentManager?.let { fm ->
            fm.findFragmentByTag(POST_COMPLAINT_FRAGMENT)?.let { fragment ->
                fm.beginTransaction().remove(fragment).commit()
            }
        }
        //finish()
        activity?.finish()
    }

    override fun onDetach() {
        super.onDetach()
        requireActivity().onBackPressedDispatcher.removeCallback(backCallback)
    }

}
