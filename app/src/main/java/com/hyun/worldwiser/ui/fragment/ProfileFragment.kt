package com.hyun.worldwiser.ui.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
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
import com.hyun.worldwiser.viewmodel.ProfileInformationViewModel
import com.hyun.worldwiser.viewmodel.ProfileSelectViewModel
import com.hyun.worldwiser.viewmodel.TourSpotsSelectViewModel
import java.lang.Exception
import java.time.format.DateTimeFormatter

class ProfileFragment : Fragment() {

    private lateinit var fragmentProfileBinding: FragmentProfileBinding
    private val intentFilter: IntentFilter = IntentFilter()
    private val insertActivity: InsertActivity = InsertActivity()

    private val adapterFilter: AdapterFilter = AdapterFilter()

    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private lateinit var country: String
    private lateinit var imageUrl: String
    private lateinit var startDay: String
    private lateinit var endDay: String

    private lateinit var spotsTitle: String
    private lateinit var popularSpotsImageUrl: String
    private lateinit var spotsAddress: String

    private val pickImageRequest = 1

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val profileSelectViewModel: ProfileSelectViewModel = ViewModelProvider(this)[ProfileSelectViewModel::class.java]
        val profileInformationViewModel: ProfileInformationViewModel = ViewModelProvider(this)[ProfileInformationViewModel::class.java]
        val tourSpotsSelectViewModel: TourSpotsSelectViewModel = ViewModelProvider(this)[TourSpotsSelectViewModel::class.java]

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

                            fragmentProfileBinding.popularSpotsRecyclerView.adapter = UserPopularTourSpotsAdapter(requireContext(), requireActivity(), popularTourSpotsList, tourSpotsSelectViewModel,)
                            fragmentProfileBinding.popularSpotsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }

        db.collection("verifications").whereEqualTo("authUid", auth.currentUser!!.uid).get()
            .addOnSuccessListener { querySnapshot ->

                for (document in querySnapshot.documents) {
                    if (isAdded) {
                        val userNickname = document["nickname"].toString()
                        val userTravelType = document["travel_preferences"].toString()
                        val userFavoriteCountry = document["country_favorite"].toString()
                        val userProfileImage = document["profileUrl"].toString()
                        val userFollowerCount = document["followerCount"].toString()

                        fragmentProfileBinding.tvProfileNickname.text = userNickname
                        fragmentProfileBinding.tvProfileTravelType.text = "선호하는 여행 타입: $userTravelType"
                        fragmentProfileBinding.tvProfileTravelTransport.text = "좋아하는 나라: $userFavoriteCountry"
                        fragmentProfileBinding.tvProfileTravelFollowerCount.text = "팔로워 수: ${userFollowerCount}명"

                        profileInformationViewModel._profileUrl.postValue(userProfileImage)

                        Glide.with(requireContext())
                            .load(userProfileImage)
                            .into(fragmentProfileBinding.ivProfileUser)

                        db.collection("travelRecommends").whereEqualTo("travelRecommendAuthUid", auth.currentUser!!.uid).get()
                            .addOnSuccessListener { travelInserts ->
                                if (isAdded) {
                                    fragmentProfileBinding.tvProfileTravelCount.text = "여행 개수: ${travelInserts.count()}개"
                                }
                            }
                    }
                }
            }

        fragmentProfileBinding.btnUserLogout.setOnClickListener {
            auth.signOut()

            val intent = Intent(requireContext(), RegisterActivity::class.java)
            startActivity(intent)
        }

        fragmentProfileBinding.ivProfileGallery.setOnClickListener {
            Log.d("ProfileFragment", "클릭")
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, pickImageRequest)
        }

        return fragmentProfileBinding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == pickImageRequest && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data

            db.collection("verifications")
                .document(auth.currentUser!!.uid)
                .update("profileUrl", selectedImageUri)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "프로필 이미지가 정상적으로 등록되었습니다!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "프로필 이미지가 등록되지 않았습니다!", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
