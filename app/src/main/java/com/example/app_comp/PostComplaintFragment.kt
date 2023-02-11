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


class PostComplaintFragment : Fragment() {
    private lateinit var binding: FragmentPostComplaintBinding
    private val viewModel: UserViewModel by viewModels()
    private lateinit var backCallback: OnBackPressedCallback
    private var images: MutableList<Uri> = mutableListOf()

    // for a single image
//    private fun chooseImage() {
//        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
//            addCategory(Intent.CATEGORY_OPENABLE)
//            type = "image/*"
//            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//        }
//        startActivityForResult(intent, REQUEST_CODE_IMAGE_PICK)
//    }

    // for multiple images

    private fun performFileSearch() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
        }
        startActivityForResult(intent, READ_REQUEST_CODE)
    }

    // for single image

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        //requestCode == REQUEST_CODE_IMAGE_PICK
//        if (requestCode == READ_REQUEST_CODE  && resultCode == RESULT_OK) {
//            images.clear()
//            if (data?.clipData != null) {
//                for (i in 0 until data.clipData!!.itemCount) {
//                    val imageUri: Uri = data.clipData!!.getItemAt(i).uri
//                    images.add(imageUri)
//                }
//            } else if (data?.data != null) {
//                images.add(data.data!!)
//            }
//        }
//    }

    // for multiple images

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.let {
                if (it.clipData != null) {
                    val clipData = it.clipData
                    if (clipData != null) {
                        for (i in 0 until clipData.itemCount) {
                            val item = clipData?.getItemAt(i)
                            val uri = item?.uri
                            // do something with the uri
                        }
                    }
                } else if (it.data != null) {
                    val uri = it.data
                    // do something with the uri
                }
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPostComplaintBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            val complaint = Complaint(
                title = binding.etTitle.text.toString(),
                description = binding.etDescription.text.toString(),
                images = images
            )
            viewModel.postComplaint(complaint)
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

