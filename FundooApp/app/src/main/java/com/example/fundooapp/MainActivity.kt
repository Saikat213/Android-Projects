package com.example.fundooapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val registerLayout = RegisterFragment()
        val loginLayout = LoginFragment()

        val register = findViewById<TextView>(R.id.registerUser)
        val login = findViewById<TextView>(R.id.Login)

        register.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainer, registerLayout)
                commit()
            }
        }

        login.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainer, loginLayout)
                commit()
            }
        }
    }
}