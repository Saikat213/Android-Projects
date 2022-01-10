package com.example.fundooapp.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val BASEURL = "https://61dbbb724593510017aff9a3.mockapi.io/fundooapp/"
class ApiClient {
    companion object {
        private var retrofit : Retrofit ?= null
        fun getApiClient() : Retrofit {
            val gson = GsonBuilder().setLenient().create()
            val httpClient = OkHttpClient.Builder().readTimeout(100, TimeUnit.SECONDS).connectTimeout(100, TimeUnit.SECONDS).build()
            if (retrofit == null) {
                retrofit = Retrofit.Builder().baseUrl(BASEURL).client(httpClient).addConverterFactory(GsonConverterFactory.create(gson)).build()
            }
            return retrofit!!
        }
    }
}