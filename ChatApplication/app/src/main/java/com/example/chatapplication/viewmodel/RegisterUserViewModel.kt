package com.example.chatapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapplication.model.AuthListener
import com.example.chatapplication.model.User
import com.example.chatapplication.model.UserAuthService

class RegisterUserViewModel(private val userAuthService: UserAuthService) : ViewModel() {
    private val _registerStatus = MutableLiveData<AuthListener>()
    var registerStatus = _registerStatus as LiveData<AuthListener>

    fun registerUser(user : User) {
        userAuthService.registerUser(user) {
            _registerStatus.value = it
        }
    }
}