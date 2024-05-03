package com.hyun.worldwiser.viewmodel

import TourApiService
import android.content.Context
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.google.gson.GsonBuilder
import com.hyun.worldwiser.adapter.TourSpotsAdapter
import com.hyun.worldwiser.databinding.FragmentHomeBinding
import com.hyun.worldwiser.model.spots.Root
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TourApiServiceViewModel : ViewModel() {

    val gson = GsonBuilder()
        .setLenient()
        .create()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://apis.data.go.kr/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()


    private val tourApiService: TourApiService = retrofit.create(TourApiService::class.java)

    fun getTourSpots(activity: FragmentActivity?, tourSpotAdapter: (TourSpotsAdapter) -> Unit) {
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
                        val adapter = TourSpotsAdapter(context, items)

                        tourSpotAdapter(adapter)
                    }
                }
            }

            override fun onFailure(call: Call<Root>, t: Throwable) {
                Log.d("ERROR", t.message.toString())
            }
        })
    }
}