package com.example.fundooapp.retrofitapi

data class AuthResponse(val registered : Boolean, val idtoken : String = "", val localID : String = "")
