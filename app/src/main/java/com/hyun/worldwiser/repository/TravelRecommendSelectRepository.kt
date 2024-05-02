package com.hyun.worldwiser.repository

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.hyun.worldwiser.adapter.HomeTravelRecommendAdapter
import com.hyun.worldwiser.databinding.FragmentHomeBinding
import com.hyun.worldwiser.model.HomeTravelRecommend

class TravelRecommendSelectRepository {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val travelRecommendList = ArrayList<HomeTravelRecommend>()

    fun travelRecommendSelect(
        isAdded: Boolean,
        context: Context,
        fragmentHomeBinding: FragmentHomeBinding,
        repositorySuccess: (String) -> Unit
    ) {
        db.collection("travelRecommends")
            .get()
            .addOnSuccessListener { querySnapshot  ->

                if (isAdded) {
                    for (document in querySnapshot.documents) {
                        val travelRecommendCountry = document["travelRecommendCountry"].toString()
                        val travelRecommendNickname = document["travelRecommendNickname"].toString()

                        if (travelRecommendCountry.isNotEmpty()) {
                            travelRecommendList.add(HomeTravelRecommend(travelRecommendCountry, travelRecommendNickname))
                        }
                    }

                    val homeTravelRecommendAdapter = HomeTravelRecommendAdapter(context, travelRecommendList)

                    fragmentHomeBinding.rvRecommendStatus.adapter = homeTravelRecommendAdapter
                    fragmentHomeBinding.rvRecommendStatus.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                }

                repositorySuccess("TravelRecommendSelectViewModel Connection!")
            }
    }
}