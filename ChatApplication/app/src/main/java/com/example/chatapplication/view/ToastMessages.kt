package com.example.chatapplication.view

import android.content.Context
import android.widget.Toast

class ToastMessages {
    fun displayMessage(context: Context, message : String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}