package com.hyun.worldwiser.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.hyun.worldwiser.databinding.FragmentHomeBinding
import com.hyun.worldwiser.repository.TravelInsertRepository

class TravelCountryRankingSelectViewModel : ViewModel() {
    private val travelInsertRepository: TravelInsertRepository = TravelInsertRepository()

    fun travelCountryRankingSelect (
        isAdded: Boolean,
        context: Context,
        fragmentHomeBinding: FragmentHomeBinding,
        viewModelSuccess: (String) -> Unit
    ) {
        travelInsertRepository.travelCountryRankingSelect(isAdded, context, fragmentHomeBinding) { repositorySuccess ->
            Log.d("TravelRecommendSelectViewModel", repositorySuccess)
            viewModelSuccess("TravelInsertViewModel Connection!")
        }
    }
}