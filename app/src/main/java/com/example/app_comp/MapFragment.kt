package com.example.app_comp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.example.app_comp.databinding.FragmentLoginBinding
import com.example.app_comp.databinding.FragmentMapBinding
import com.example.app_comp.utils.PREFS_KEY
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient

class MapFragment : Fragment() {

    private lateinit var binding: FragmentMapBinding
    private lateinit var placesClient: PlacesClient
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap

    override fun onResume() {
        super.onResume()
        (activity as? UserActivity)?.setCurrentFragment(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? UserActivity)?.setCurrentFragment(this)
        // Initialize Places API
        Places.initialize(requireContext(), "AIzaSyC9luSucLGA3VdYrVorE6ajD_Qh0grcYBE")
        placesClient = Places.createClient(requireContext())

        // Initialize Fused Location Provider
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        mapView = view.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { map ->
            googleMap = map
            // Customize the map settings, markers, etc.
            // ...
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapBinding.inflate(layoutInflater)


        // Assuming you have a button named btnSetLocation
        binding.btnSetLocation.setOnClickListener {
            // Get the current location
            val currentLocation = getCurrentLocation()

            // Save the location to SharedPreferences
            saveLocationToSharedPreferences(currentLocation.toString())

            // Go back to the previous fragment (PostComplaintFragment)
            requireActivity().supportFragmentManager.popBackStack()
        }



        return binding.root
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    val currentLocation = "${location.latitude}, ${location.longitude}"
                    saveLocationToSharedPreferences(currentLocation)
                    requireActivity().supportFragmentManager.popBackStack()
                } else {
                    // Location is null, handle the case when the location is unavailable
                    // ...
                }
            }
            .addOnFailureListener { exception ->
                // Handle the error
                // ...
            }
    }

    private fun saveLocationToSharedPreferences(location: String) {
        val sharedPreferences = requireContext().getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("LOCATION", location)
        editor.apply()
    }

    // Assuming you have an EditText named etSearchLocation
    private fun searchLocation(query: String) {
        // Perform the search based on the query
        val searchedLocation = performLocationSearch(query)

        // Save the searched location to SharedPreferences
        saveLocationToSharedPreferences(searchedLocation.toString())

        // Go back to the previous fragment (PostComplaintFragment)
        requireActivity().supportFragmentManager.popBackStack()
    }

    private fun performLocationSearch(query: String) {
        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(query)
            .build()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response ->
                // Handle the search results
                val predictions = response.autocompletePredictions
                // Process the predictions and display them to the user
                // ...
                // Save the selected location to SharedPreferences
                val selectedLocation = predictions[0].getPrimaryText(null).toString()
                saveLocationToSharedPreferences(selectedLocation)
                requireActivity().supportFragmentManager.popBackStack()
            }
            .addOnFailureListener { exception ->
                // Handle the error
                // ...
            }
    }

}