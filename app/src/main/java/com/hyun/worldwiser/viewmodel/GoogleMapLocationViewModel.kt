package com.hyun.worldwiser.viewmodel

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task

class GoogleMapLocationViewModel(
    private val context: Context,
    private val mActivity: Activity,
) : ViewModel() {

    private val _userCurrentLocation: MutableLiveData<Location> = MutableLiveData<Location>()
    val userCurrentLocation: MutableLiveData<Location> = _userCurrentLocation

    var mFusedLocationProviderClient: MutableLiveData<FusedLocationProviderClient> = MutableLiveData()
    val FINE_PERMISSION_CODE = 200

    fun getLastLocation() {
        mFusedLocationProviderClient.value = LocationServices.getFusedLocationProviderClient(context)

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(mActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), FINE_PERMISSION_CODE)
            return
        }

        val task: Task<Location>? = mFusedLocationProviderClient.value?.lastLocation

        task?.addOnSuccessListener { location ->

            if (location != null) {
                setLastLocation(location)
            }
        }
    }

    private fun setLastLocation(location: Location) {
        _userCurrentLocation.postValue(location)
    }
}