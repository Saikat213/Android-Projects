package com.example.chatapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.chatapplication.model.UserAuthService

class VerifyOtpViewModelFactory(val userAuthService: UserAuthService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return VerifyOtpViewModel(userAuthService) as T
    }
}