package com.hyun.worldwiser.ui.spots

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hyun.worldwiser.R
import com.hyun.worldwiser.databinding.ActivitySpotsDetailBinding
import com.hyun.worldwiser.viewmodel.TourSpotsSelectViewModel

class SpotsDetailActivity : AppCompatActivity() {

    private lateinit var activitySpotsDetailBinding: ActivitySpotsDetailBinding
    private lateinit var tourSpotsSelectViewModel: TourSpotsSelectViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activitySpotsDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_spots_detail)

        tourSpotsSelectViewModel = ViewModelProvider(this).get(TourSpotsSelectViewModel::class.java)

        activitySpotsDetailBinding.lifecycleOwner = this
        activitySpotsDetailBinding.tourSpotsSelectViewModel = tourSpotsSelectViewModel

        val tourSpotsTitle = intent.getStringExtra("TourSpotsTitle")

        if (tourSpotsTitle != null) {
            tourSpotsSelectViewModel.setTourSpotsTitle(tourSpotsTitle)
        }

        Log.d("SpotsDetailActivity1", tourSpotsSelectViewModel.tourSpotsTitle.value.toString())
    }
}