package com.example.chatapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.chatapplication.utility.FirebaseService

class ChatDetailsViewModelFactory(val firebaseService: FirebaseService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChatDetailsViewModel(firebaseService) as T
    }
}