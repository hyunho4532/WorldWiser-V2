package com.hyun.worldwiser.repository

import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TourSpotsPopularRepository {
    private val firebaseStore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    fun getTourSpotsPopular(title: String, address: String, imageUrl: String) {

        val popularSpots = hashMapOf (
            "authUid" to auth.currentUser!!.uid,
            "title" to title,
            "address" to address,
            "imageUrl" to imageUrl
        )

        firebaseStore.collection("tourSpots").add(popularSpots).addOnSuccessListener {

        }
    }
}