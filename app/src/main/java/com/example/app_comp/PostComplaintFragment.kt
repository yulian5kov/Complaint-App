package com.example.app_comp

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_comp.data.Complaint
import com.example.app_comp.databinding.FragmentPostComplaintBinding
import com.example.app_comp.utils.CAMERA_REQUEST_CODE
import com.example.app_comp.utils.GALLERY_REQUEST_CODE
import com.example.app_comp.utils.Result
import com.example.app_comp.utils.mAuth
import com.example.app_comp.utils.showToast
import com.google.android.gms.location.LocationServices
import com.google.firebase.Timestamp
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import android.location.Location
import androidx.core.content.ContextCompat
import com.example.app_comp.utils.LOCATION_PERMISSION_REQUEST_CODE
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.FusedLocationProviderClient





class PostComplaintFragment : Fragment() {

    private lateinit var binding: FragmentPostComplaintBinding
    private val viewModel: UserViewModel by viewModels()
    private lateinit var photosDialog: AlertDialog
    // Declare a list to hold the image URIs
    private val imageUris: MutableList<Uri> = mutableListOf()
    private lateinit var imageAdapter: ImageAdapter


    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var currentLocation: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? UserActivity)?.setCurrentFragment(this)
    }

    override fun onResume() {
        (activity as? UserActivity)?.setCurrentFragment(this)
        super.onResume()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostComplaintBinding.inflate(layoutInflater)

        photosDialog = AlertDialog.Builder(requireContext())
            .setTitle("Which one")
            .setMessage("Take a photo or pick from existing")
            .setIcon(R.drawable.ic_question_foreground)
            .setPositiveButton("Camera"){ _,_ ->
                cameraCheckPermission()
            }
            .setNegativeButton("Gallery"){ _,_ ->
                galleryCheckPermission()
            }.create()

        // Create an instance of your custom ImageAdapter
        imageAdapter = ImageAdapter(requireContext(), imageUris)

        // Set the adapter for the RecyclerView
        binding.rvImages.adapter = imageAdapter
        binding.rvImages.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.btnPhotos.setOnClickListener {
            Log.d("photos", imageUris.toString())
            photosDialog.show()
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        binding.btnLocation.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val locationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(10000) // Interval in milliseconds to request location updates

                val locationCallback = object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        locationResult?.let {
                            val location = it.lastLocation
                            val geocoder = Geocoder(requireContext(), Locale.getDefault())
                            val addresses = location?.let { it1 -> geocoder.getFromLocation(it1.latitude, location.longitude, 1) }
                            if (addresses != null) {
                                if (addresses.isNotEmpty()) {
                                    currentLocation = (addresses?.get(0)?.getAddressLine(0) ?: Log.d("Location", "Current Location: $currentLocation")) as String
                                    binding.etLocation.setText(currentLocation)
                                } else {
                                    Log.e("Location", "No addresses found")
                                }
                            }
                            // Remove location updates after retrieving the location
                            fusedLocationProviderClient.removeLocationUpdates(this)
                        }
                    }
                }

                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
            } else {
                requestLocationPermission()
            }
        }




        binding.btnSend.setOnClickListener{
            showToast("KUR")
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
            val date = Date()
            val dateString = dateFormat.format(date)
            val timestamp = Timestamp(date)
            val images =  imageUris.map { it.toString() }

            val title = binding.etTitle.text.toString()
            val description = binding.etDesc.text.toString()

            if(title.isEmpty() || description.isEmpty() || images.isEmpty()){
                showToast("The complaint won't be sent without title, description or images")
                return@setOnClickListener
            }

            val complaint = Complaint(
                id = "",
                userId = mAuth.currentUser!!.uid,
                title = title,
                description = description,
                images = images,
                date = timestamp,
                location = binding.etLocation.text.toString(),
                status = "Pending"
            )

            viewModel.postComplaint(complaint)
                .onStart {
                    Log.d("postComplaint", "post complaint has started")
                }
                .onEach { result ->
                    when (result) {
                        is Result.Success<Complaint> -> {
                            showToast("Complaint sent successfully")
                            requireActivity().onBackPressed()
                        }

                        is Result.Loading -> {}
                        is Result.Failed -> {}
                        is Result.Error -> {}
                    }
                }
                .launchIn(lifecycleScope)
        }

        return binding.root
    }

    private fun camera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST_CODE -> {
                    val bitmap = data?.extras?.get("data") as Bitmap
                    val uri = saveImage(bitmap)
                    imageUris.add(uri)
                    imageAdapter.notifyDataSetChanged()
                }

                GALLERY_REQUEST_CODE -> {
                    val clipData = data?.clipData
                    if (clipData != null) {
                        for (i in 0 until clipData.itemCount) {
                            val selectedImageUri = clipData.getItemAt(i).uri
                            imageUris.add(selectedImageUri)
                        }
                    } else {
                        val selectedImageUri = data?.data
                        selectedImageUri?.let {
                            imageUris.add(it)
                        }
                    }
                    imageAdapter.notifyDataSetChanged()
                }
            }
        }

    }

    private fun saveImage(bitmap: Bitmap): Uri {
        // Save the bitmap to a file and return the file URI
        // You need to implement this logic according to your requirements
        // Here's an example implementation using MediaStore:

        val resolver = requireContext().contentResolver
        val contentValues = ContentValues()
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "image_${UUID.randomUUID()}.jpg")
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")

        val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        imageUri?.let { uri ->
            val outputStream = resolver.openOutputStream(uri)
            outputStream?.use { stream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            }
        }

        return imageUri ?: Uri.EMPTY
    }

    private fun gallery() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }


    private fun galleryCheckPermission() {
        Dexter.withContext(requireContext())
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    gallery()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    showToast("Permission denied for gallery")
                    showRationaleDialogForPermission()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    showRationaleDialogForPermission()
                }
            }).check()
    }

    private fun showRationaleDialogForPermission() {
        AlertDialog.Builder(requireContext())
            .setMessage("It looks like you have denied the required permission. Enable permission through the app settings to access this feature.")
            .setPositiveButton("Go to Settings") { _, _ ->
                openAppSettings()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", requireContext().packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    private fun cameraCheckPermission() {
        Dexter.withContext(requireContext())
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if (report.areAllPermissionsGranted()) {
                            camera()
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    showRationaleDialogForPermission()
                }
            })
            .check()
    }

    // LOCATION

    private fun requestLocationPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, retry the location retrieval
                binding.btnLocation.performClick()
            } else {
                // Permission denied, show a message or handle it accordingly
                showToast("Location permission denied")
            }
        }
    }

}