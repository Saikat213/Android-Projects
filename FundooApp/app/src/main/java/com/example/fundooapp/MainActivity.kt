package com.example.fundooapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fundooapp.model.UserAuthService
import com.example.fundooapp.viewmodel.SharedViewModel
import com.example.fundooapp.viewmodel.SharedViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedViewModel = ViewModelProvider(
            this,
            SharedViewModelFactory(UserAuthService())
        )[SharedViewModel::class.java]

        sharedViewModel.gotoLoginPage(true)
        observeAppNav()

        //val registerLayout = RegisterFragment()
        //val loginLayout = LoginFragment()

//        val register = findViewById<TextView>(R.id.registerUser)
//        val login = findViewById<TextView>(R.id.Login)
//
//        register.setOnClickListener {
//            supportFragmentManager.beginTransaction().apply {
//                replace(R.id.fragmentContainer, registerLayout)
//                commit()
//            }
//        }
//
//        login.setOnClickListener {
//            supportFragmentManager.beginTransaction().apply {
//                replace(R.id.fragmentContainer, loginLayout)
//                commit()
//            }
//        }
    }

    private fun observeAppNav() {
        sharedViewModel.gotoLoginPageStatus.observe(this, Observer {
            if (it == true) {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragmentContainer, LoginFragment())
                    commit()
                }

            }
        })
        sharedViewModel.gotoRegistrationPageStatus.observe(this, Observer {
            if (it == true) {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragmentContainer, RegisterFragment())
                    commit()
                }
            }
        })
        sharedViewModel.gotoHomePageStatus.observe(this, Observer {
            if (it == true) {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragmentContainer, HomeFragment())
                    commit()
                }
            }
        })
    }

    private fun loadLoginPage() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainer, LoginFragment())
            commit()
        }
    }

    private fun initActivity() {

    }
}