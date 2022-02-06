package com.example.chatapplication.utility

import com.example.chatapplication.model.Constants.FCM_BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class RetrofitInstance {
    companion object {
        private val retrofit by lazy {
            Retrofit.Builder().baseUrl(FCM_BASE_URL).addConverterFactory(ScalarsConverterFactory.create())
                .build()
        }
        val api by lazy {
            retrofit.create(NotificationApi::class.java)
        }
    }
}