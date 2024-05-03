package com.hyun.worldwiser.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.hyun.worldwiser.databinding.FragmentHomeBinding
import com.hyun.worldwiser.repository.TravelRecommendSelectRepository

class TravelRecommendViewModel : ViewModel() {

    private val travelRecommendSelectRepository: TravelRecommendSelectRepository = TravelRecommendSelectRepository()

    fun travelRecommendSelect(isAdded: Boolean, context: Context, fragmentHomeBinding: FragmentHomeBinding, viewModelSuccess: (String) -> Unit) {
        travelRecommendSelectRepository.travelRecommendSelect(isAdded, context, fragmentHomeBinding) { repositorySuccess ->
            Log.d("TravelRecommendSelectViewModel", repositorySuccess)
            viewModelSuccess("TravelRecommendSelectViewModel Connection!")
        }
    }
}