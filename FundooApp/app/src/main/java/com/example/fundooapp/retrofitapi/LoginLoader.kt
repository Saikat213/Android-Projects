package com.example.fundooapp.retrofitapi

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginLoader {
    fun getLogin(listener: LoginListener, email: String, password: String) {
        ApiClient.getInstance()
        ApiClient.getApi().loginFundoouser(LoginRequest(email, password, true))
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful) {
                        listener.onLogin(response.body()!!, true, "")
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    listener.onLogin(null, false, t.message.toString())
                }
            })
    }
}