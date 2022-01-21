package com.example.chatapplication.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.chatapplication.R
import com.example.chatapplication.model.UserAuthService
import com.example.chatapplication.viewmodel.SharedViewModel
import com.example.chatapplication.viewmodel.SharedViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedViewModel =
            ViewModelProvider(this, SharedViewModelFactory(UserAuthService()))[SharedViewModel::class.java]
        splashScreen()
        observeAppNav()
    }

    private fun observeAppNav() {
        sharedViewModel.gotoHomePageStatus.observe(this, Observer {
            if (it) {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragmentContainer, HomeFragment())
                    commit()
                }
            }
        })
        sharedViewModel.gotoRegisterPageStatus.observe(this, Observer {
            if (it) {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragmentContainer, RegisterUserFragment())
                    commit()
                }
            }
        })
        sharedViewModel.gotoVerifyOtpPageStatus.observe(this, Observer {
            if (it) {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragmentContainer, VerifyOtp())
                    commit()
                }
            }
        })
        sharedViewModel.gotoGetOtpPageStatus.observe(this, Observer {
            if (it) {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragmentContainer, GetOtpFragment())
                    commit()
                }
            }
        })
    }

    private fun splashScreen() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainer, SplashScreen())
            commit()
        }
    }
}