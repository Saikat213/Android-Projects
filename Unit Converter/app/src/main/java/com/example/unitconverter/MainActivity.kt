package com.example.unitconverter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val convertFragment = ConvertFragment()
        val addFragment = AddQuantityFragment()
        val convert_button = findViewById<Button>(R.id.convertbutton)
        val addButton = findViewById<Button>(R.id.addbutton)

        convert_button.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, convertFragment)
                commit()
            }
        }
        addButton.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, addFragment)
                commit()
            }
        }
    }
}