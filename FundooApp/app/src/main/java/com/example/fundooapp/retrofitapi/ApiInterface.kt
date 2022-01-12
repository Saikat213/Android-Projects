package com.example.fundooapp.retrofitapi

import com.example.fundooapp.model.NotesData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInterface {
    @GET("notes")
    fun fetchAllData() : Call<NotesData>

    @POST("./accounts:signInWithPassword?key=AIzaSyAIs4r7w0ZBGgso2XUMgb3SiJ2C6fCQmKk")
    fun loginFundoouser(@Body request : LoginRequest) : Call<LoginResponse>
}