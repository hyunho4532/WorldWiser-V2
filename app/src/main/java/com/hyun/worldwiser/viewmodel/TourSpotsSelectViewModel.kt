package com.hyun.worldwiser.viewmodel

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hyun.worldwiser.lib.GoogleMapLatLngModules
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.hyun.worldwiser.model.CurrentLocation
import com.hyun.worldwiser.model.TourSpotsSelect
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


class TourSpotsSelectViewModel : ViewModel() {
    private val _tourSpotsTitle = MutableLiveData<String>()
    private val _tourSpotsAddress = MutableLiveData<String>()

    private val _tourSpotsLatitude = MutableLiveData<Double>()
    private val _tourSpotsLongitude = MutableLiveData<Double>()

    private val _currentLocationLatitude = MutableLiveData<Double>()
    private val _currentLocationLongitude = MutableLiveData<Double>()

    val tourSpotsTitle: LiveData<String>
        get() = _tourSpotsTitle

    val tourSpotsAddress: LiveData<String>
        get() = _tourSpotsAddress

    val tourSpotsLatitude: LiveData<Double>
        get() = _tourSpotsLatitude

    val tourSpotsLongitude: LiveData<Double>
        get() = _tourSpotsLongitude

    val currentLocationLatitude: LiveData<Double>
        get() = _currentLocationLatitude

    val currentLocationLongitude: LiveData<Double>
        get() = _currentLocationLongitude

    fun setTourSpots(tourSpots: ArrayList<TourSpotsSelect>) {
        tourSpots.forEach { tourSpotsSelect ->
            _tourSpotsTitle.value = tourSpotsSelect.title
            _tourSpotsAddress.value = tourSpotsSelect.address
            _tourSpotsLatitude.value = tourSpotsSelect.tourSpotsLatitude
            _tourSpotsLongitude.value = tourSpotsSelect.tourSpotsLongitude
        }
    }

    fun setCurrentLocation(currentLocation: ArrayList<CurrentLocation>) {
        currentLocation.forEach { location ->
            Log.d("TourSpotsSelectViewModel", location.currentLongitude.toString())
            _currentLocationLatitude.value = location.currentLatitude
            _currentLocationLongitude.value = location.currentLongitude
        }
    }

    fun setCalculateDistanceSpots(currentLet: Double, currentLng: Double, spotLat: Double, spotLng: Double): Double {

        // 지구 평균 반지름: km
        val earthRadius = 6371

        val latFromRadians = Math.toRadians(spotLat - currentLet)
        val lonFromRadians = Math.toRadians(spotLng - currentLng)

        val calculateBetweenTwoPoints =
            sin(latFromRadians / 2) * sin(latFromRadians / 2) +
                    cos(Math.toRadians(currentLet)) * cos(Math.toRadians(spotLat)) *
                    sin(lonFromRadians / 2) * sin(lonFromRadians / 2)

        val calculateDistanceBetweenTwoPoints = 2 * atan2(sqrt(calculateBetweenTwoPoints), sqrt(1 - calculateBetweenTwoPoints))

        return earthRadius * calculateDistanceBetweenTwoPoints
    }

    fun handleCurrentLocation (
        googleMap: GoogleMap,
        tourSpotsOfLatLng: LatLng,
        currentLat: Double,
        currentLng: Double
    ) {

        val googleMapLatLngModules = GoogleMapLatLngModules()

        val currentLocationOfLatLng = LatLng (
            currentLat,
            currentLng
        )

        googleMapLatLngModules.addMarker(googleMap, currentLocationOfLatLng, tourSpotsOfLatLng)

        val polylineOptions = PolylineOptions()
            .color(Color.BLACK)
            .width(30F)
            .add(tourSpotsOfLatLng)
            .add(currentLocationOfLatLng)

        googleMap.moveCamera (
            CameraUpdateFactory.newLatLngZoom(
                tourSpotsOfLatLng,
                15.0f
            )
        )

        googleMap.addPolyline(polylineOptions)
    }

}