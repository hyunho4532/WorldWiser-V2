package com.hyun.worldwiser.lib

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng

interface LatLng {
    fun setLatLng(currentLat: Double, currentLng: Double, resultLatLng: (LatLng) -> Unit) {

    }
}