package com.example.chatapplication.model

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging

class UserAuthService {
    private val firebaseAuth : FirebaseAuth
    private val firebaseDatabase : FirebaseDatabase

    init {
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
    }

    fun signInWithCredential(phoneAuthCredential: PhoneAuthCredential, listener: (AuthListener) -> Unit) {
        firebaseAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener {
            if (it.isSuccessful) {
                listener(AuthListener(true, "Success"))
            } else {
                listener(AuthListener(false, "Authorization Failed"))
            }
        }
    }

    fun fetchFcmToken() : String {
        var token : String ?= null
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (!it.isSuccessful) {
                Log.d("Token Error--->", "${it.exception}")
                return@addOnCompleteListener
            }
            token = it.result
            Log.d("Token--->", "$token")
        }
        return token!!
    }
}