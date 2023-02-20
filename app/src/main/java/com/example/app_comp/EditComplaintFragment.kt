package com.example.app_comp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.example.app_comp.databinding.FragmentEditComplaintBinding

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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