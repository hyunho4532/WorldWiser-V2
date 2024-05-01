package com.hyun.worldwiser.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hyun.worldwiser.R
import com.hyun.worldwiser.adapter.TravelAdapter
import com.hyun.worldwiser.adapter.TravelSwipeToDeleteCallback
import com.hyun.worldwiser.adapter.UserPopularTourSpotsAdapter
import com.hyun.worldwiser.databinding.FragmentProfileBinding
import com.hyun.worldwiser.model.Travel
import com.hyun.worldwiser.model.UserTourSpots
import com.hyun.worldwiser.ui.register.RegisterActivity
import com.hyun.worldwiser.ui.travel.InsertActivity
import com.hyun.worldwiser.util.AdapterFilter
import com.hyun.worldwiser.util.IntentFilter
import com.hyun.worldwiser.viewmodel.DateTimeFormatterViewModel
import com.hyun.worldwiser.viewmodel.ProfileSelectViewModel
import java.lang.Exception
import java.time.format.DateTimeFormatter

class ProfileFragment : Fragment() {

    private lateinit var fragmentProfileBinding: FragmentProfileBinding
    private val intentFilter: IntentFilter = IntentFilter()
    private val insertActivity: InsertActivity = InsertActivity()

    private val adapterFilter: AdapterFilter = AdapterFilter()

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private lateinit var country: String
    private lateinit var imageUrl: String
    private lateinit var startDay: String
    private lateinit var endDay: String

    private lateinit var spotsTitle: String
    private lateinit var popularSpotsImageUrl: String
    private lateinit var spotsAddress: String

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val profileSelectViewModel: ProfileSelectViewModel = ViewModelProvider(this)[ProfileSelectViewModel::class.java]

        val travelList = ArrayList<Travel>()
        val popularTourSpotsList = ArrayList<UserTourSpots>()

        fragmentProfileBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        fragmentProfileBinding.btnTravelInsert.setOnClickListener {
            intentFilter.getIntent(requireContext(), insertActivity)
        }

        profileSelectViewModel.getTravelInserts()

        profileSelectViewModel.country.observe(requireActivity()) { country ->
            this.country = country
        }

        db.collection("travelInserts").whereEqualTo("authUid", auth.currentUser!!.uid).get()
            .addOnSuccessListener { querySnapshot  ->

                for (document in querySnapshot.documents) {

                    if (isAdded) {
                        try {
                            country = document["country"].toString()
                            imageUrl = document["imageUrl"].toString()
                            startDay = document["startDay"].toString()
                            endDay = document["endDay"].toString()

                            Glide.with(requireActivity())
                                .load(imageUrl)
                                .into(fragmentProfileBinding.imageView)

                            fragmentProfileBinding.tvTravelCountry.text = country

                            fragmentProfileBinding.tvTravelCalendar.text = "$startDay ~ $endDay"

                            val travel = Travel(imageUrl, country, startDay, endDay)
                            travelList.add(travel)

                            val travelAdapter = TravelAdapter(requireContext(), travelList)

                            val itemTouchHelper = ItemTouchHelper(TravelSwipeToDeleteCallback(travelAdapter))
                            adapterFilter.getAdapter(recyclerView = requireView().findViewById(R.id.travelRecyclerView), travelAdapter = travelAdapter, context = requireContext(), itemTouchHelper = itemTouchHelper)

                            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

                            val dateTimeFormatterViewModel = ViewModelProvider(this)[DateTimeFormatterViewModel::class.java]

                            dateTimeFormatterViewModel.settingDateTimeFormatter(formatter, startDay)
                            fragmentProfileBinding.dateTimeFormatterViewModel = dateTimeFormatterViewModel

                        } catch (e: UninitializedPropertyAccessException) {
                            fragmentProfileBinding.tvTravelCalendar.text = ""
                        }
                    }
                }
            }

        db.collection("tourSpots").whereEqualTo("authUid", auth.currentUser!!.uid).get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    if (isAdded) {
                        try {
                            spotsTitle = document["title"].toString()
                            popularSpotsImageUrl = document["imageUrl"].toString()
                            spotsAddress = document["address"].toString()

                            val userTourSpots = UserTourSpots(spotsTitle, popularSpotsImageUrl, spotsAddress)
                            popularTourSpotsList.add(userTourSpots)

                            fragmentProfileBinding.popularSpotsRecyclerView.adapter = UserPopularTourSpotsAdapter(requireContext(), popularTourSpotsList)
                            fragmentProfileBinding.popularSpotsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }

        fragmentProfileBinding.btnUserLogout.setOnClickListener {
            auth.signOut()

            val intent = Intent(requireContext(), RegisterActivity::class.java)
            startActivity(intent)
        }


        return fragmentProfileBinding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }
}
