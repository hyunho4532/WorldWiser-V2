package com.hyun.worldwiser.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hyun.worldwiser.R
import com.hyun.worldwiser.databinding.FragmentHomeBinding
import com.hyun.worldwiser.util.HomeFragmentTitleFilter
import com.hyun.worldwiser.viewmodel.TourApiServiceViewModel
import com.hyun.worldwiser.viewmodel.TravelCountryRankingSelectViewModel
import com.hyun.worldwiser.viewmodel.TravelCountryStatusSelectViewModel
import com.hyun.worldwiser.viewmodel.TravelRecommendViewModel

class HomeFragment : Fragment() {

    private lateinit var fragmentHomeBinding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        initializeDataBinding(inflater, container)

        val homeFragmentTitleFilter = HomeFragmentTitleFilter(fragmentHomeBinding)
        homeFragmentTitleFilter.homeFragmentTitleSettings()

        initializeViewModels()

        return fragmentHomeBinding.root
    }

    private fun initializeDataBinding(inflater: LayoutInflater, container: ViewGroup?) {
        fragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
    }

    private fun initializeViewModels () {

        val travelRecommendViewModel = ViewModelProvider(this)[TravelRecommendViewModel::class.java]
        val travelCountryRankingSelectViewModel = ViewModelProvider(this)[TravelCountryRankingSelectViewModel::class.java]
        val travelCountryStatusSelectViewModel = ViewModelProvider(this)[TravelCountryStatusSelectViewModel::class.java]
        val tourApiServiceViewModel = ViewModelProvider(this)[TourApiServiceViewModel::class.java]

        executeViewModels (travelRecommendViewModel, travelCountryRankingSelectViewModel, travelCountryStatusSelectViewModel, tourApiServiceViewModel)
    }

    private fun executeViewModels(
        travelRecommendViewModel: TravelRecommendViewModel,
        travelCountryRankingSelectViewModel: TravelCountryRankingSelectViewModel,
        travelCountryStatusSelectViewModel: TravelCountryStatusSelectViewModel,
        tourApiServiceViewModel: TourApiServiceViewModel
    ) {
        travelRecommendViewModel.travelRecommendSelect(isAdded, requireContext()) { adapter ->
            fragmentHomeBinding.rvRecommendStatus.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            fragmentHomeBinding.rvRecommendStatus.adapter = adapter
        }

        travelCountryRankingSelectViewModel.travelCountryRankingSelect(isAdded, requireContext()) { adapter ->
            fragmentHomeBinding.rvTravelRanking.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            fragmentHomeBinding.rvTravelRanking.adapter = adapter
        }

        travelCountryStatusSelectViewModel.travelCountryStatusSelect(isAdded, requireContext()) { adapter ->
            fragmentHomeBinding.rvTravelStatus.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            fragmentHomeBinding.rvTravelStatus.adapter = adapter
        }

        tourApiServiceViewModel.getTourSpots(activity) { adapter ->
            fragmentHomeBinding.rvRecommendSpot.adapter = adapter
            fragmentHomeBinding.rvRecommendSpot.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }
}