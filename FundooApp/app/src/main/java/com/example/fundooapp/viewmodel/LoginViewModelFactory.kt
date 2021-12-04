package com.example.fundooapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fundooapp.model.UserAuthService

class LoginViewModelFactory(val userAuthService: UserAuthService) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(userAuthService) as T
    }
}