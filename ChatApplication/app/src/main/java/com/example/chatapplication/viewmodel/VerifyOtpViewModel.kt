package com.example.chatapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapplication.model.AuthListener
import com.example.chatapplication.model.UserAuthService
import com.google.firebase.auth.PhoneAuthCredential

class VerifyOtpViewModel(val userAuthService: UserAuthService) : ViewModel() {
    private val _signInWithCredentialStatus = MutableLiveData<AuthListener>()
    var signInWithCredential = _signInWithCredentialStatus as LiveData<AuthListener>

    fun signInWithCredential(credential: PhoneAuthCredential) {
        userAuthService.signInWithCredential(credential) {
            if (it.status)
                _signInWithCredentialStatus.value = it
        }
    }
}