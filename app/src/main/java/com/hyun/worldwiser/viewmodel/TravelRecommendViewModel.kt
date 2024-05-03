package com.hyun.worldwiser.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.hyun.worldwiser.adapter.HomeTravelRecommendAdapter
import com.hyun.worldwiser.adapter.TravelRecommendAdapter
import com.hyun.worldwiser.databinding.FragmentHomeBinding
import com.hyun.worldwiser.repository.TravelRecommendSelectRepository

class TravelRecommendViewModel : ViewModel() {

    private val travelRecommendSelectRepository: TravelRecommendSelectRepository = TravelRecommendSelectRepository()

    fun travelRecommendSelect(isAdded: Boolean, context: Context, adapter: (HomeTravelRecommendAdapter) -> Unit) {
        travelRecommendSelectRepository.travelRecommendSelect(isAdded, context) { homeTravelRecommendAdapter ->
            adapter(homeTravelRecommendAdapter)
        }
    }
}