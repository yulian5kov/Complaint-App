package com.example.app_comp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.example.app_comp.databinding.FragmentPostComplaintBinding
import com.example.app_comp.databinding.FragmentViewComplaintBinding

class ViewComplaintFragment : Fragment() {

    private lateinit var binding: FragmentViewComplaintBinding

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
            (activity as UserActivity).setButtonInvisible()
            return binding.root
        }catch (e: Exception){
            Log.e(DEBUGGING, "vcf: Error in onCreateView: ${e.message}")
            return null
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
        (activity as UserActivity).setButtonVisible()
    }

}