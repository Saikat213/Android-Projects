package com.example.fundooapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundooapp.model.AuthListener
import com.example.fundooapp.model.UserAuthService

class LoginViewModel(val userAuthService: UserAuthService) : ViewModel() {
    private var _loginStatus = MutableLiveData<AuthListener>()
    val loginStatus = _loginStatus as LiveData<AuthListener>

    fun loginToFundoo(emailID : String, password : String) {
        userAuthService.loginUser(emailID, password){
            _loginStatus.value = it
        }
    }

    fun loginWithApi(emailID: String, password: String) {
        userAuthService.loginWithRestApi(emailID, password) {
            _loginStatus.value = it
        }
    }
}
