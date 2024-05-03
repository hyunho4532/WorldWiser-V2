package com.hyun.worldwiser.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.hyun.worldwiser.databinding.FragmentHomeBinding
import com.hyun.worldwiser.repository.TravelCountryStatusSelectRepository

class TravelCountryStatusSelectViewModel : ViewModel() {

    private val travelCountryStatusSelectRepository: TravelCountryStatusSelectRepository = TravelCountryStatusSelectRepository()

    fun travelCountryStatusSelect(isAdded: Boolean, context: Context, fragmentHomeBinding: FragmentHomeBinding, success: (String) -> Unit) {
        travelCountryStatusSelectRepository.travelCountryStatusSelect(isAdded, context, fragmentHomeBinding) { repositorySuccess ->
            Log.d("TravelCountryStatusViewModel", repositorySuccess)
        }
    }
}