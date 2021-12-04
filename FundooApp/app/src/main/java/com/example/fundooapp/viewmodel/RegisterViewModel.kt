package com.example.fundooapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundooapp.model.AuthListener
import com.example.fundooapp.model.User
import com.example.fundooapp.model.UserAuthService

class RegisterViewModel(val userAuthService: UserAuthService) : ViewModel() {
    private var _registerStatus = MutableLiveData<AuthListener>()
    var registerStatus = _registerStatus as LiveData<AuthListener>

    fun registerUser(user: User) {
        userAuthService.registerUser(user){
            _registerStatus.value = it
        }
    }
}