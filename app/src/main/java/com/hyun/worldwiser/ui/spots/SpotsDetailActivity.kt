package com.hyun.worldwiser.ui.spots

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.hyun.worldwiser.R
import com.hyun.worldwiser.databinding.ActivitySpotsDetailBinding
import com.hyun.worldwiser.model.CurrentLocation
import com.hyun.worldwiser.model.TourSpotsSelect
import com.hyun.worldwiser.viewmodel.TourSpotsSelectViewModel
import java.io.IOException
import java.lang.NullPointerException
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this )

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
                            val address = addresses[0].getAddressLine(0)
                            val currentLat = location.latitude
                            val currentLng = location.longitude

                            tourSpotsSelectViewModel.setCurrentLocation (
                                arrayListOf (
                                    CurrentLocation(currentLat, currentLng)
                                )
                            )

                            activitySpotsDetailBinding.tvTourSpotsGps.text = address

                            val spotLat = tourSpotsSelectViewModel.tourSpotsLatitude.value
                            val spotLng = tourSpotsSelectViewModel.tourSpotsLongitude.value

                            if (spotLat != null && spotLng != null) {
                                val distance = tourSpotsSelectViewModel.setCalculateDistanceSpots(currentLat, currentLng, spotLat, spotLng)
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

        try {

            Log.d("SpotsDetailActivity", "관광지 위치: ${tourSpotsSelectViewModel.tourSpotsLatitude.value.toString()}")

            val tourSpotsOfLatLng = LatLng (
                tourSpotsSelectViewModel.tourSpotsLatitude.value!!,
                tourSpotsSelectViewModel.tourSpotsLongitude.value!!
            )

            tourSpotsSelectViewModel.currentLocationLatitude.observe(this) { lat ->

                if (lat != null) {
                    tourSpotsSelectViewModel.currentLocationLongitude.observe(this) { lng ->

                        Log.d("SpotsDetailActivity", "현재 위치: $lat $lng")

                        googleMap.addMarker(
                            MarkerOptions()
                                .position(tourSpotsOfLatLng)
                                .title("Marker in Sydney")
                        )

                        val currentLocation = LatLng(lat, lng)
                        googleMap.addMarker(
                            MarkerOptions()
                                .position(currentLocation)
                                .title("Current Location")
                        )

                        googleMap.moveCamera (
                            CameraUpdateFactory.newLatLngZoom(
                                tourSpotsOfLatLng,
                                15.0f
                            )
                        )
                    }
                }
            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }
}