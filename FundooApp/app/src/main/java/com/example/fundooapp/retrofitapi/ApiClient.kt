package com.example.fundooapp.retrofitapi

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val BASEURLFORDATA = "https://61dbbb724593510017aff9a3.mockapi.io/fundooapp/"
const val BASEURLFIREBASE = "https://identitytoolkit.googleapis.com/v1/"

object ApiClient {
    private lateinit var myApi : ApiInterface
    private var instance : ApiClient ?= null
    private var httpClient: OkHttpClient

    init {
        httpClient = OkHttpClient.Builder().readTimeout(100, TimeUnit.SECONDS)
            .connectTimeout(100, TimeUnit.SECONDS).build()
    }

    fun getInstance() : ApiClient? {
        if (instance == null) {
            instance = ApiClient
        }
        return instance
    }

    fun getApi(): ApiInterface {
        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder().baseUrl(BASEURLFIREBASE).client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gson)).build()
        myApi = retrofit.create(ApiInterface::class.java)
        return myApi
    }
}