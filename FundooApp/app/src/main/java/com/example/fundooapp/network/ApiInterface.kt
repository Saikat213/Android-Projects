package com.example.fundooapp.network

import com.example.fundooapp.model.NotesData
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {
    @GET("posts./accounts:signInWithPassword?key=AIzaSyAIs4r7w0ZBGgso2XUMgb3SiJ2C6fCQmKk/")
    fun fetchAllData() : Call<List<NotesData>>
}