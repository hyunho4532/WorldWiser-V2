package com.hyun.worldwiser.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TourSpotsSelectViewModel : ViewModel() {
    private val _tourSpotsTitle = MutableLiveData<String>()
    val tourSpotsTitle : MutableLiveData<String> = _tourSpotsTitle

    fun setTourSpotsTitle(tourSpots: String) {
        tourSpotsTitle.postValue(tourSpots)
    }
}