package com.hyun.worldwiser.ui.fragment

import TourApiService
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.GsonBuilder
import com.hyun.worldwiser.R
import com.hyun.worldwiser.adapter.CountryRankingAdapter
import com.hyun.worldwiser.adapter.TourSpotsAdapter
import com.hyun.worldwiser.adapter.TravelStatusAdapter
import com.hyun.worldwiser.databinding.FragmentHomeBinding
import com.hyun.worldwiser.model.CountryRanking
import com.hyun.worldwiser.model.TravelStatus
import com.hyun.worldwiser.model.spots.Root
import com.hyun.worldwiser.util.HomeFragmentTitleFilter
import com.hyun.worldwiser.viewmodel.TravelRecommendSelectViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeFragment : Fragment() {

    private lateinit var fragmentHomeBinding: FragmentHomeBinding

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val countryRankingList = ArrayList<CountryRanking>()
    private val travelStatusList = ArrayList<TravelStatus>()

    private val uniqueCountries = HashSet<String>()

    val gson = GsonBuilder()
        .setLenient()
        .create()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://apis.data.go.kr/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val tourApiService = retrofit.create(TourApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        fragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        val homeFragmentTitleFilter = HomeFragmentTitleFilter(fragmentHomeBinding)
        homeFragmentTitleFilter.homeFragmentTitleSettings()

        val travelRecommendInsertViewModel = ViewModelProvider(this)[TravelRecommendSelectViewModel::class.java]

        travelRecommendInsertViewModel.travelRecommendSelect(isAdded, requireContext(), fragmentHomeBinding) { viewModelSuccess ->
            Log.d("HomeFragment", viewModelSuccess)
        }

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
                        val countryRankingAdapter = CountryRankingAdapter(requireContext(), countryRankingList)

                        fragmentHomeBinding.rvTravelRanking.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                        fragmentHomeBinding.rvTravelRanking.adapter = countryRankingAdapter
                    }
                }
            }


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

                    val travelStatusAdapter = TravelStatusAdapter(requireContext(), travelStatusList)

                    fragmentHomeBinding.rvTravelStatus.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    fragmentHomeBinding.rvTravelStatus.adapter = travelStatusAdapter
                }
            }

        tourApiService.getTourSpots (
            serviceKey = "KmXwF4GXnRJiiNY68ky5tSl88Zi3IsotZW3VlDC%2BEGf472pLAf%2FgWmsnJDq9d22bOLATJFTTixhypw6BuSDJug%3D%3D", numOfRows = 10,
            pageNo = 1, mobileOS = "ETC",
            mobileApp = "AppTest", listYN = "Y",
            arrange = "A", keyword = "강원", contentTypeId = 12).enqueue(object: Callback<Root> {

            override fun onResponse(call: Call<Root>, response: Response<Root>) {
                if (response.isSuccessful) {
                    val items = response.body()?.response!!.body.items.item

                    val context = activity?.applicationContext

                    if (context != null) {
                        val adapter = TourSpotsAdapter(requireContext(), items)

                        fragmentHomeBinding.rvRecommendSpot.adapter = adapter
                        fragmentHomeBinding.rvRecommendSpot.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    }
                }
            }

            override fun onFailure(call: Call<Root>, t: Throwable) {
                Log.d("ERROR", t.message.toString())
            }
        })

        return fragmentHomeBinding.root
    }
}