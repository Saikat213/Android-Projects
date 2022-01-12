package com.example.fundooapp.retrofitapi

object Constants {
    var constant : Constants ?= null
    private lateinit var userID : String

    fun getInstance() : Constants? {
        if (constant == null) {
            constant = Constants
        }
        return constant
    }

    fun setUserId(userID : String) {
        this.userID = userID
    }
}