package com.hyun.worldwiser.ui.spots

import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.hyun.worldwiser.R
import com.hyun.worldwiser.databinding.ActivitySpotsDetailBinding
import com.hyun.worldwiser.model.TourSpotsSelect
import com.hyun.worldwiser.viewmodel.TourSpotsSelectViewModel

class SpotsDetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var activitySpotsDetailBinding: ActivitySpotsDetailBinding
    private lateinit var tourSpotsSelectViewModel: TourSpotsSelectViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val geocoder: Geocoder = Geocoder(this)

        activitySpotsDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_spots_detail)

        tourSpotsSelectViewModel = ViewModelProvider(this).get(TourSpotsSelectViewModel::class.java)

        activitySpotsDetailBinding.lifecycleOwner = this
        activitySpotsDetailBinding.tourSpotsSelectViewModel = tourSpotsSelectViewModel

        val tourSpotsTitle = intent.getStringExtra("TourSpotsTitle")
        val tourSpotsAddress = intent.getStringExtra("TourSpotsAddress")

        if (tourSpotsTitle != null) {
            tourSpotsSelectViewModel.setTourSpots (
                arrayListOf(TourSpotsSelect(tourSpotsTitle, tourSpotsAddress!!))
            )

            geocodeAddress(tourSpotsAddress, geocoder)
        }

        activitySpotsDetailBinding.mapView.onCreate(savedInstanceState)
        activitySpotsDetailBinding.mapView.onResume()
        activitySpotsDetailBinding.mapView.getMapAsync(this)
    }

    private fun geocodeAddress(tourSpotsAddress: String, geocoder: Geocoder) {
        val cors = geocoder.getFromLocationName(tourSpotsAddress, 1)
        Toast.makeText(this, "결과: ${cors!![0].latitude}", Toast.LENGTH_SHORT).show()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val sydney = LatLng(-33.852, 151.211)
        googleMap.addMarker(
            MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney")
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}