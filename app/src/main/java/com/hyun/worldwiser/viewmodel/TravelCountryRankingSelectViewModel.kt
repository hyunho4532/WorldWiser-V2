package com.hyun.worldwiser.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.hyun.worldwiser.adapter.CountryRankingAdapter
import com.hyun.worldwiser.databinding.FragmentHomeBinding
import com.hyun.worldwiser.repository.TravelInsertRepository

class TravelCountryRankingSelectViewModel : ViewModel() {
    private val travelInsertRepository: TravelInsertRepository = TravelInsertRepository()

    fun travelCountryRankingSelect (
        isAdded: Boolean,
        context: Context,
        countryRankingAdapter: (CountryRankingAdapter) -> Unit
    ) {
        travelInsertRepository.travelCountryRankingSelect(isAdded, context) { adapter ->
            countryRankingAdapter(adapter)
        }
    }
}