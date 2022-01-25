package com.example.chatapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapplication.model.AuthListener
import com.example.chatapplication.model.User
import com.example.chatapplication.utility.FirebaseService

class UpdateProfileViewModel(private val firebaseService: FirebaseService) : ViewModel() {
    private val _updateUserProfileDetails = MutableLiveData<AuthListener>()
    val updateUserProfileDetails = _updateUserProfileDetails as LiveData<AuthListener>

    fun updateUserProfile(user: User) {
        firebaseService.updateUserProfile(user) {
            if (it.status)
                _updateUserProfileDetails.value = it
        }
    }
}