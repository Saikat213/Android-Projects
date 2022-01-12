package com.example.fundooapp.retrofitapi

interface LoginListener {
    fun onLogin(response: LoginResponse?, status: Boolean, message: String)
}