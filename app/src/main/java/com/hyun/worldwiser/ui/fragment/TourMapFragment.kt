package com.hyun.worldwiser.ui.fragment

import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.hyun.worldwiser.R
import com.hyun.worldwiser.viewmodel.GoogleMapLocationViewModel

class TourMapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var googleMapLocationViewModel: GoogleMapLocationViewModel

    private lateinit var currentLocation: Location

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        googleMapLocationViewModel = GoogleMapLocationViewModel(requireContext(), requireActivity())

        googleMapLocationViewModel.userCurrentLocation.observe(viewLifecycleOwner) { location ->
            currentLocation = location
        }

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        val tourMapView = view?.findViewById<MapView>(R.id.google_map_view)
        tourMapView?.onCreate(savedInstanceState)
        tourMapView?.onResume()
        tourMapView?.getMapAsync(this)

        googleMapLocationViewModel.getLastLocation()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tour_map, container, false)
    }

    override fun onMapReady(googleMap: GoogleMap) {

        googleMap.let {
            if (::currentLocation.isInitialized) {
                googleMap.addMarker(
                    MarkerOptions()
                        .position(LatLng(currentLocation.latitude, currentLocation.longitude))
                        .title("Marker")
                )

                googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(currentLocation.latitude, currentLocation.longitude)))
            }
        }
    }
}