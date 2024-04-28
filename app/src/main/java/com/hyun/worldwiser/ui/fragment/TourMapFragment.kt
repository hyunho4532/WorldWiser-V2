package com.hyun.worldwiser.ui.fragment

import TourLocationApiService
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.GsonBuilder
import com.hyun.worldwiser.R
import com.hyun.worldwiser.model.tourSpots.Item
import com.hyun.worldwiser.model.tourSpots.Tour
import com.hyun.worldwiser.viewmodel.GoogleMapLocationViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TourMapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var googleMapLocationViewModel: GoogleMapLocationViewModel

    private lateinit var currentLocation: Location
    private var googleMarkerList: List<Item> = emptyList()

    val gson = GsonBuilder()
        .setLenient()
        .create()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://apis.data.go.kr/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val tourLocationApiService = retrofit.create(TourLocationApiService::class.java)

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        googleMapLocationViewModel = GoogleMapLocationViewModel(requireContext(), requireActivity())

        googleMapLocationViewModel.userCurrentLocation.observe(viewLifecycleOwner) { location ->
            currentLocation = location
        }

        if (::currentLocation.isInitialized) {
            Log.d("TourMapFragment", currentLocation.longitude.toString())
        }

        if (::currentLocation.isInitialized) {
            tourLocationApiService.getLocationBasedList (
                serviceKey = "04DBvw1Cg29V1OVRIBBuWWtdWD%2BJR56nz6mzbsQeyGILb7K4QmN78QipJNAdeG%2BNQPovWEPNwnkpYq1OHVLhZA%3D%3D", numOfRows = 10,
                pageNo = 1, mobileOS = "AND",
                mobileApp = "AppTest", listYN = "Y",
                arrange = "A", mapX = currentLocation.longitude,
                mapY = currentLocation.latitude, radius = 2000,
                contentTypeId = 15).enqueue(object: Callback<Tour> {

                override fun onResponse(call: Call<Tour>, response: Response<Tour>) {
                    if (response.isSuccessful) {
                        val items = response.body()?.response?.body?.items?.item

                        Log.d("TourMapFragment", items.toString())

                        items?.let {
                            googleMarkerList += it
                        }
                    }
                }

                override fun onFailure(call: Call<Tour>, t: Throwable) {
                    Log.d("ERROR", t.message.toString())
                }
            })
        }

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        val tourMapView = view?.findViewById<MapView>(R.id.google_map_view)
        tourMapView?.onCreate(savedInstanceState)
        tourMapView?.onResume()
        tourMapView?.getMapAsync(this)

        googleMapLocationViewModel.getLastLocation()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tour_map, container, false)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        showMarkerOnMap(googleMap)
    }

    private fun showMarkerOnMap(googleMap: GoogleMap) {
        if (::currentLocation.isInitialized) {
            googleMarkerList.forEach { item ->
                val position = LatLng(item.mapy.toDouble(), item.mapx.toDouble())
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15.0f))
                googleMap.addMarker(
                    MarkerOptions()
                        .position(position)
                        .title(item.title)
                )
            }
        }
    }
}