package com.hyun.worldwiser.lib

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng

interface Marker {
    fun addMarker(googleMap: GoogleMap, vararg latLng: LatLng) {

    }
}