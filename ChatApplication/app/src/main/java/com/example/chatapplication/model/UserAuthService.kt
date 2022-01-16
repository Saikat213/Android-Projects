package com.example.chatapplication.model

import androidx.lifecycle.ViewModelProvider
import com.example.chatapplication.viewmodel.SharedViewModel
import com.google.firebase.auth.FirebaseAuth

class UserAuthService {
    private val firebaseAuth : FirebaseAuth

    init {
        firebaseAuth = FirebaseAuth.getInstance()
    }

    fun registerUser(user: User, listener: (AuthListener) -> Unit) {
    }
}