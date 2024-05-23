package com.hyun.worldwiser.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class TourSpotsSelectViewModel : ViewModel() {
    private val _tourSpotsTitle = MutableLiveData<String>()
    val tourSpotsTitle: LiveData<String>
        get() = _tourSpotsTitle

    fun setTourSpotsTitle(tourSpots: String) {

        _tourSpotsTitle.value = tourSpots
    }
}