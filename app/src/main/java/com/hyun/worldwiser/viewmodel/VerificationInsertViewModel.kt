package com.hyun.worldwiser.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.hyun.worldwiser.databinding.ActivityVerificationBinding
import com.hyun.worldwiser.repository.VerificationInsertRepository
import com.hyun.worldwiser.util.HashMapOfFilter

class VerificationInsertViewModel : ViewModel() {

    private val hashMapOf: HashMapOfFilter = HashMapOfFilter()

    private val verificationInsertRepository: VerificationInsertRepository = VerificationInsertRepository { success ->
        _verificationResults.postValue(success)
    }

    private val _verificationResults = MutableLiveData<Boolean>()
    val verificationResults: LiveData<Boolean> = _verificationResults

    fun insertVerification(
        countriesString: String,
        activityVerificationBinding: ActivityVerificationBinding,
        auth: FirebaseAuth
    ) {

        val verification = hashMapOf.insertVerificationDataFromMap (
            auth.currentUser!!.uid,
            countriesString,
            activityVerificationBinding.powerSpinnerView.text.toString(),
            activityVerificationBinding.powerSpinnerView2.text.toString(),
            activityVerificationBinding.etNicknameTextFormField.text.toString(),
            "",
            0
        )

        verificationInsertRepository.insertVerification(verification)
    }
}