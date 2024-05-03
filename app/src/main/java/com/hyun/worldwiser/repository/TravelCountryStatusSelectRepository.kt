package com.hyun.worldwiser.repository

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.hyun.worldwiser.adapter.TravelStatusAdapter
import com.hyun.worldwiser.databinding.FragmentHomeBinding
import com.hyun.worldwiser.model.TravelStatus

class TravelCountryStatusSelectRepository {

    private val db = FirebaseFirestore.getInstance()

    private val uniqueCountries = HashSet<String>()
    private val travelStatusList = ArrayList<TravelStatus>()

    fun travelCountryStatusSelect(
        isAdded: Boolean,
        context: Context,
        fragmentHomeBinding: FragmentHomeBinding,
        repositorySuccess: (String) -> Unit
    ) {
        db.collection("travelInserts")
            .get()
            .addOnSuccessListener { querySnapshot  ->

                if (isAdded) {
                    val travelStatusCountMap = HashMap<String, Int>()

                    for (document in querySnapshot.documents) {
                        val countryStatus = document["countryStatus"].toString()

                        val count = travelStatusCountMap.getOrDefault(countryStatus, 0)
                        travelStatusCountMap[countryStatus] = count + 1

                        uniqueCountries.add(countryStatus)
                    }

                    travelStatusList.clear()

                    travelStatusCountMap.forEach { (country, count) ->
                        travelStatusList.add(TravelStatus(country, count))
                    }

                    travelStatusList.sortByDescending {
                        it.countryStatusCount
                    }

                    val travelStatusAdapter = TravelStatusAdapter(context, travelStatusList)

                    fragmentHomeBinding.rvTravelStatus.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    fragmentHomeBinding.rvTravelStatus.adapter = travelStatusAdapter
                }
            }
    }
}