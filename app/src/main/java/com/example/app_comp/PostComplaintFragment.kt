package com.example.app_comp

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
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
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class PostComplaintFragment : Fragment() {
    private lateinit var binding: FragmentPostComplaintBinding
    private val viewModel: UserViewModel by viewModels()
    private lateinit var backCallback: OnBackPressedCallback
    private var images: MutableList<Uri> = mutableListOf()

    // for multiple images
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            Log.d(DEBUGGING, "resultcode equals $resultCode and RESULT_OK equals $RESULT_OK")
            if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                data?.let {
                    if (it.clipData != null) {
                        val clipData = it.clipData
                        if (clipData != null) {
                            for (i in 0 until clipData.itemCount) {
                                val item = clipData?.getItemAt(i)
                                val uri = item?.uri
                                if (uri != null) {
                                    images.add(uri)
                                }
                                Log.d(DEBUGGING, "Selected image URI: $uri")
                            }
                        }
                    } else if (it.data != null) {
                        val uri = it.data
                        if (uri != null) {
                            images.add(uri)
                        }
                        Log.d(DEBUGGING, "Selected image URI: $uri")
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(DEBUGGING, "Error in onActivityResult: ${e.message}")
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        try {
            // Inflate the layout for this fragment
            binding = FragmentPostComplaintBinding.inflate(inflater, container, false)
            return binding.root
        } catch (e: Exception) {
            Log.e(DEBUGGING, "Error in onCreateView: ${e.message}")
            return null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
        } catch (e: Exception) {
            Log.e(DEBUGGING, "Error in onCreate: ${e.message}")
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userActivity = activity as UserActivity
        val userbinding = userActivity.getBinding()

        binding.btnAddImage.setOnClickListener {
            val intent = Intent().apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            }
            startActivityForResult(
                Intent.createChooser(intent, "Select Images"),
                REQUEST_CODE_IMAGE_PICK
            )
        }

        binding.btnPostComplaint.setOnClickListener {
            Log.d(DEBUGGING, "Button click listener registered")
            val complaint = Complaint(
                title = binding.etTitle.text.toString(),
                description = binding.etDescription.text.toString(),
                images = images
            )
            //viewModel.postComplaint(complaint)
            lifecycleScope.launch {
                when (val result = viewModel.postComplaint(complaint).first()) {
                    is Result.Success -> {
                        Log.d(DEBUGGING, "Complaint added successfully")
                        Snackbar.make(userbinding.root, "Complaint posted", Snackbar.LENGTH_LONG).show()
                    }
                    is Result.Error -> {
                        Log.d(DEBUGGING, "Error posting complaint: ${result.exception}")
                        Snackbar.make(userbinding.root, "Error posting complaint", Snackbar.LENGTH_LONG).show()
                    }
                    is Result.Failed -> {
                        Log.d(DEBUGGING, "Error posting complaint: ${result.error} message:${result.message}")
                        Snackbar.make(userbinding.root, "Failed posting complaint", Snackbar.LENGTH_LONG).show()
                    }
                    is Result.Loading -> {
                        Log.d(DEBUGGING, "Loading posting complaint: ${result.isLoading}")
                        showProgress()
                    }
                }
            }
            requireActivity().supportFragmentManager.popBackStack()
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


}

