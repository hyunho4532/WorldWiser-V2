package com.hyun.worldwiser.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hyun.worldwiser.model.TourSpotsSelect
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.time.times


class TourSpotsSelectViewModel : ViewModel() {
    private val _tourSpotsTitle = MutableLiveData<String>()
    private val _tourSpotsAddress = MutableLiveData<String>()
    private val _tourSpotsLatitude = MutableLiveData<Double>()
    private val _tourSpotsLongitude = MutableLiveData<Double>()

    val tourSpotsTitle: LiveData<String>
        get() = _tourSpotsTitle

    val tourSpotsAddress: LiveData<String>
        get() = _tourSpotsAddress

    val tourSpotsLatitude: LiveData<Double>
        get() = _tourSpotsLatitude

    val tourSpotsLongitude: LiveData<Double>
        get() = _tourSpotsLongitude

    fun setTourSpots(tourSpots: ArrayList<TourSpotsSelect>) {
        tourSpots.forEach { tourSpotsSelect ->
            _tourSpotsTitle.value = tourSpotsSelect.title
            _tourSpotsAddress.value = tourSpotsSelect.address
            _tourSpotsLatitude.value = tourSpotsSelect.latitude
            _tourSpotsLongitude.value = tourSpotsSelect.longitude
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
}