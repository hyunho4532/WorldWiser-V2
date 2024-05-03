package com.hyun.worldwiser.repository

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.hyun.worldwiser.adapter.CountryRankingAdapter
import com.hyun.worldwiser.databinding.FragmentHomeBinding
import com.hyun.worldwiser.model.CountryRanking

class TravelInsertRepository {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val uniqueCountries = HashSet<String>()
    private val countryRankingList = ArrayList<CountryRanking>()

    fun travelCountryRankingSelect (
        isAdded: Boolean,
        context: Context,
        adapter: (CountryRankingAdapter) -> Unit
    ) {
        db.collection("travelInserts")
            .get()
            .addOnSuccessListener { querySnapshot  ->

                if (isAdded) {
                    val countryCountMap = HashMap<String, Int>()

                    for (document in querySnapshot.documents) {
                        val country = document["country"].toString()

                        val count = countryCountMap.getOrDefault(country, 0)
                        countryCountMap[country] = count + 1

                        uniqueCountries.add(country)
                    }

                    countryRankingList.clear()

                    countryCountMap.forEach { (country, count) ->
                        countryRankingList.add(CountryRanking(country, count))
                    }

                    countryRankingList.sortByDescending {
                        it.countryRankingCount
                    }

                    if (countryRankingList.isNotEmpty()) {
                        val countryRankingAdapter = CountryRankingAdapter(context, countryRankingList)

                        adapter(countryRankingAdapter)
                    }
                }
            }
    }
}