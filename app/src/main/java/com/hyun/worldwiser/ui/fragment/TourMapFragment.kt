package com.hyun.worldwiser.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentContainerView
import com.google.android.gms.dynamic.SupportFragmentWrapper
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.OnMapsSdkInitializedCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.hyun.worldwiser.R

class TourMapFragment : Fragment(), OnMapReadyCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MapsInitializer.initialize(requireContext())
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tour_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tourMapView = view.findViewById<MapView>(R.id.google_map_view)
        tourMapView.getMapAsync(this)
        tourMapView.onCreate(savedInstanceState)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.addMarker(
            MarkerOptions()
                .position(LatLng(0.0, 0.0))
                .title("Marker")
        )
        googleMap.setMinZoomPreference(15.0f)
        googleMap.setMaxZoomPreference(20.0f)
    }
}