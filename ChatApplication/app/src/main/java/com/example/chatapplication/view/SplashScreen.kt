package com.example.chatapplication.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.chatapplication.R
import com.example.chatapplication.model.Constants
import com.example.chatapplication.model.CustomSharedPreference
import com.example.chatapplication.model.UserAuthService
import com.example.chatapplication.viewmodel.SharedViewModel
import com.example.chatapplication.viewmodel.SharedViewModelFactory
import com.google.firebase.auth.FirebaseAuth

class SplashScreen : Fragment() {
    private lateinit var splashImage : ImageView
    private lateinit var appTitle : TextView
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        splashImage = view.findViewById(R.id.appIcon)
        appTitle = view.findViewById(R.id.appName)
        sharedViewModel = ViewModelProvider(requireActivity(), SharedViewModelFactory(
            UserAuthService()
        ))[SharedViewModel::class.java]
        splashImage.alpha = 0f
        splashImage.animate().setDuration(2000).alpha(1f).withEndAction {
            if (FirebaseAuth.getInstance().currentUser != null) {
                sharedViewModel.gotoHomePageStatus(true)
            } else {
                val currentUser = FirebaseAuth.getInstance().currentUser?.phoneNumber
                if (currentUser != null) {
                    CustomSharedPreference.initSharedPreference(requireContext())
                    CustomSharedPreference.addString(Constants.EXISTING_USER, currentUser)
                }
                sharedViewModel.gotoGetOtpPageStatus(true)
                activity?.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
        }
    }
}