package com.hyun.worldwiser.util

class HashMapOfFilter {
    fun insertVerificationDataFromMap (
        authUid: String,
        countryFavorite: String,
        travelPreferences: String,
        transport: String,
        nickname: String,
        profileUrl: String,
        followerCount: Int
    ): HashMap<String, Any> {

        return hashMapOf (
            "authUid" to authUid,
            "country_favorite" to countryFavorite,
            "travel_preferences" to travelPreferences,
            "transport" to transport,
            "nickname" to nickname,
            "profileUrl" to profileUrl,
            "followerCount" to followerCount
        )
    }
}