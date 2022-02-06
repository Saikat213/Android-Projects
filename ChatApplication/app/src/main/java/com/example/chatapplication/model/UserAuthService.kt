package com.example.chatapplication.model

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.chatapplication.utility.RetrofitInstance
import com.example.chatapplication.view.ToastMessages
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

    fun fetchFcmToken() {
        var token : String
        FirebaseMessaging.getInstance().token.addOnCompleteListener{
            if (!it.isSuccessful) {
                Log.d("Token Error--->", "${it.exception}")
                return@addOnCompleteListener
            }
            token = it.result
        }
    }

    fun pushNotifications(context : Context, notification : String) {
        RetrofitInstance.api.postNotification(notification).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.d("ResponseCode--->", response.code().toString())
                if (response.isSuccessful) {
                    if (response.body()!!.isNotEmpty()) {
                        Log.d("success--->", response.body().toString())
                        ToastMessages().displayMessage(context, "Notified")
                    }
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
            }
        })
    }
}