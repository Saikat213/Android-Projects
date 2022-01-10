package com.example.fundooapp.network

import com.example.fundooapp.model.NotesData
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {
    @GET("notes")
    fun fetchAllData() : Call<NotesData>
}