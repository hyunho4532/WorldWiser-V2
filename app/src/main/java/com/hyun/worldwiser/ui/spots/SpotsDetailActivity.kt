package com.hyun.worldwiser.ui.spots

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.hyun.worldwiser.R
import com.hyun.worldwiser.databinding.ActivitySpotsDetailBinding
import com.hyun.worldwiser.model.CurrentLocation
import com.hyun.worldwiser.model.TourSpotsSelect
import com.hyun.worldwiser.viewmodel.TourSpotsSelectViewModel
import java.io.IOException

class SpotsDetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var activitySpotsDetailBinding: ActivitySpotsDetailBinding
    private lateinit var tourSpotsSelectViewModel: TourSpotsSelectViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val geocoder: Geocoder = Geocoder(this)

        activitySpotsDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_spots_detail)

        tourSpotsSelectViewModel = ViewModelProvider(this).get(TourSpotsSelectViewModel::class.java)

        activitySpotsDetailBinding.lifecycleOwner = this
        activitySpotsDetailBinding.tourSpotsSelectViewModel = tourSpotsSelectViewModel

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val tourSpotsTitle = intent.getStringExtra("TourSpotsTitle")
        val tourSpotsAddress = intent.getStringExtra("TourSpotsAddress")

        if (tourSpotsTitle != null) {
            geocodeAddress(tourSpotsTitle, tourSpotsAddress!!)
        }

        activitySpotsDetailBinding.mapView.onCreate(savedInstanceState)
        activitySpotsDetailBinding.mapView.onResume()
        activitySpotsDetailBinding.mapView.getMapAsync(this)

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            getLastLocation(geocoder)
        }

        if (tourSpotsTitle != null && tourSpotsAddress != null) {
            geocodeAddress(tourSpotsTitle, tourSpotsAddress)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getLastLocation(geocoder: Geocoder) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    try {

                        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)

                        if (addresses!!.isNotEmpty()) {

                            tourSpotsSelectViewModel.setCurrentLocation (
                                arrayListOf (
                                    CurrentLocation(addresses[0].getAddressLine(0), location.latitude, location.longitude)
                                )
                            )

                            if (tourSpotsSelectViewModel.tourSpotsLongitude.value != null && tourSpotsSelectViewModel.tourSpotsLatitude.value != null) {
                                val distance =
                                    tourSpotsSelectViewModel.setCalculateDistanceSpots(location.latitude, location.longitude, tourSpotsSelectViewModel.tourSpotsLongitude.value!!, tourSpotsSelectViewModel.tourSpotsLatitude.value!!)

                                activitySpotsDetailBinding.tvTourSpotsDistance.text = String.format("%.1f", distance) + "km"
                            }
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    private fun geocodeAddress(tourSpotsTitle: String, tourSpotsAddress: String) {
        val geocoder = Geocoder(this)
        val cors = geocoder.getFromLocationName(tourSpotsAddress, 1)

        if (cors!!.isNotEmpty()) {
            tourSpotsSelectViewModel.setTourSpots (
                arrayListOf (
                    TourSpotsSelect(tourSpotsTitle, tourSpotsAddress, cors[0].latitude, cors[0].longitude)
                )
            )
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {

        val tourSpotsOfLatLng = LatLng (
            tourSpotsSelectViewModel.tourSpotsLatitude.value!!,
            tourSpotsSelectViewModel.tourSpotsLongitude.value!!
        )

        tourSpotsSelectViewModel.currentLocationLatitude.observe(this) { currentLat ->
            tourSpotsSelectViewModel.currentLocationLongitude.observe(this) { currentLng ->

                if (currentLat != null && currentLng != null) {
                    tourSpotsSelectViewModel.handleCurrentLocation(googleMap, tourSpotsOfLatLng, currentLat, currentLng)
                }
            }
        }
    }
}