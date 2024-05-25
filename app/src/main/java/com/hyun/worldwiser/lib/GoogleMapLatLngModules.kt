package com.hyun.worldwiser.lib

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class GoogleMapLatLngModules : Marker {
    override fun addMarker(googleMap: GoogleMap, vararg latLng: LatLng) {
        super.addMarker(googleMap, *latLng)

        for (lat in latLng) {
            googleMap.addMarker(
                MarkerOptions()
                    .position(lat)
                    .title("Marker in Sydney")
            )
        }
    }
}