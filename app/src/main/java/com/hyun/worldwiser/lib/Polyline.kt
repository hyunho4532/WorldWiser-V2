package com.hyun.worldwiser.lib

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng

interface Polyline {
    fun polylineOptions(googleMap: GoogleMap, tourSpotsOfLatLng: LatLng, currentLocationOfLatLng: LatLng) {

    }
}