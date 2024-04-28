package com.hyun.worldwiser.ui.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.hyun.worldwiser.R

class TourMapFragment : Fragment(), OnMapReadyCallback {

    private val FINE_PERMISSION_CODE = 1
    private lateinit var mGoogleMap: GoogleMap
    private lateinit var mCurrentLocation: Location
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        val tourMapView = view?.findViewById<MapView>(R.id.google_map_view)

        tourMapView?.onCreate(savedInstanceState)
        tourMapView?.onResume()
        tourMapView?.getMapAsync(this)

        getLastLocation()
    }

    private fun getLastLocation() {

         if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
             ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), FINE_PERMISSION_CODE)
             return
        }

        val task: Task<Location> = mFusedLocationProviderClient.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null) {
                mCurrentLocation = location


            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tour_map, container, false)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        getLastLocation()
        googleMap.let {
            if (::mCurrentLocation.isInitialized) {
                googleMap.addMarker(
                    MarkerOptions()
                        .position(com.google.android.gms.maps.model.LatLng(mCurrentLocation.latitude, mCurrentLocation.longitude))
                        .title("Marker")
                )
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult (
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            }
        }
    }
}