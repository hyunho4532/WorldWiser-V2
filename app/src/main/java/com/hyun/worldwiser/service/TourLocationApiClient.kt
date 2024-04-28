package com.hyun.worldwiser.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object TourLocationApiClient {
    private const val BASE_URL = "https://apis.data.go.kr"

    val tourLocationApiService: TourLocationApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TourLocationApiService::class.java)
    }
}