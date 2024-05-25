package com.hyun.worldwiser.lib

import android.graphics.Color
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions

class GoogleMapModules : Marker, com.hyun.worldwiser.lib.LatLng, Polyline, Camera {

    override fun setLatLng(currentLat: Double, currentLng: Double, resultLatLng: (LatLng) -> Unit) {
        super.setLatLng(currentLat, currentLng) { setLatLng ->
            resultLatLng(setLatLng)
        }

        val currentLocationOfLatLng = LatLng (
            currentLat,
            currentLng
        )

        resultLatLng(currentLocationOfLatLng)
    }

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

    override fun polylineOptions(googleMap: GoogleMap, tourSpotsOfLatLng: LatLng, currentLocationOfLatLng: LatLng) {
        super.polylineOptions(googleMap, tourSpotsOfLatLng, currentLocationOfLatLng)

        val polylineOptions = PolylineOptions()
            .color(Color.BLACK)
            .width(30F)
            .add(tourSpotsOfLatLng)
            .add(currentLocationOfLatLng)

        googleMap.addPolyline(polylineOptions)
    }

    override fun moveCamera(googleMap: GoogleMap, tourSpotsOfLatLng: LatLng) {
        super.moveCamera(googleMap, tourSpotsOfLatLng)

        googleMap.moveCamera (
            CameraUpdateFactory.newLatLngZoom(
                tourSpotsOfLatLng,
                15.0f
            )
        )
    }
}