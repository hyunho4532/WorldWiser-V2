package com.hyun.worldwiser.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileInformationViewModel : ViewModel() {

    val _profileUrl: MutableLiveData<String> = MutableLiveData()

    val profileUrl: LiveData<String> = _profileUrl
}