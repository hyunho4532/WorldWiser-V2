package com.hyun.worldwiser.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hyun.worldwiser.model.TourSpotsSelect


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
}