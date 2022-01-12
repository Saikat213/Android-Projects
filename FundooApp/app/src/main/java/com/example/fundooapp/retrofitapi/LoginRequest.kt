package com.example.fundooapp.retrofitapi

data class LoginRequest(var email : String = "", var password : String = "", var returnSecureToken : Boolean)