package com.example.chatapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapplication.model.AuthListener
import com.example.chatapplication.model.UserMessages
import com.example.chatapplication.utility.FirebaseService

class ChatDetailsViewModel(private val firebaseService: FirebaseService) : ViewModel() {
    private val _uploadChatDetails = MutableLiveData<AuthListener>()
    val uploadChatDetails = _uploadChatDetails as LiveData<AuthListener>

    fun sendMessageToUser(userMessages: UserMessages) {
        firebaseService.uploadUserChats(userMessages) {
            if (it.status)
                _uploadChatDetails.value = it
        }
    }
}