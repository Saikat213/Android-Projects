package com.example.chatapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapplication.model.AuthListener
import com.example.chatapplication.model.User
import com.example.chatapplication.utility.FirebaseService

class RegisterUserViewModel(private val firebaseService: FirebaseService) : ViewModel() {
    private val _registerUserDetailsToFirebase = MutableLiveData<AuthListener>()
    val registerUserDetailsToFirebase = _registerUserDetailsToFirebase as LiveData<AuthListener>

    fun addUserDetails(user : User) {
        firebaseService.addUserInfoToDatabase(user) {
            if (it.status)
                _registerUserDetailsToFirebase.value = it
        }
    }
}