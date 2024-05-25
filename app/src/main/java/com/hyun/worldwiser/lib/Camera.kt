package com.hyun.worldwiser.lib

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng

interface Camera {
    fun moveCamera(googleMap: GoogleMap, tourSpotsOfLatLng: LatLng) {

    }
}