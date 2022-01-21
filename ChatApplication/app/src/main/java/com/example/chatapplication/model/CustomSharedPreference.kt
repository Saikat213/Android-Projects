package com.example.chatapplication.model

import android.content.Context
import android.content.SharedPreferences

object CustomSharedPreference {
    private var sharedPref: SharedPreferences? = null
    fun initSharedPreference(context: Context) {
        sharedPref = context.getSharedPreferences("ChatSharedPreference", Context.MODE_PRIVATE)
    }

    fun addString(key: String, value: String) {
        val editor = sharedPref?.edit()
        if (editor != null) {
            editor.putString(key, value)
            editor.apply()
        }
    }

    fun get(key: String): String? = sharedPref?.getString(key, "")
}