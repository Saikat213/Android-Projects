package com.example.chatapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapplication.model.UserAuthService

class SharedViewModel(val userAuthService: UserAuthService) : ViewModel() {
    private val _gotoRegisterPageStatus = MutableLiveData<Boolean>()
    val gotoRegisterPageStatus = _gotoRegisterPageStatus as LiveData<Boolean>

    private val _gotoHomePageStatus = MutableLiveData<Boolean>()
    val gotoHomePageStatus = _gotoHomePageStatus as LiveData<Boolean>

    private val _gotoVerifyOtpPageStatus = MutableLiveData<Boolean>()
    val gotoVerifyOtpPageStatus = _gotoVerifyOtpPageStatus as LiveData<Boolean>

    private val _gotoGetOtpPageStatus = MutableLiveData<Boolean>()
    val gotoGetOtpPageStatus = _gotoGetOtpPageStatus as LiveData<Boolean>

    private val _gotoUpdateProfilePageStatus = MutableLiveData<Boolean>()
    val gotoUpdateProfilePageStatus = _gotoUpdateProfilePageStatus as LiveData<Boolean>

    fun gotoHomePageStatus(status : Boolean) {
        _gotoHomePageStatus.value = status
    }

    fun gotoRegisterPageStatus(status: Boolean) {
        _gotoRegisterPageStatus.value = status
    }

    fun gotoVerifyOtpPageStatus(status: Boolean) {
        _gotoVerifyOtpPageStatus.value = status
    }

    fun gotoGetOtpPageStatus(status: Boolean) {
        _gotoGetOtpPageStatus.value = status
    }

    fun gotoUpdateProfilePageStatus(status: Boolean) {
        _gotoUpdateProfilePageStatus.value = status
    }
}