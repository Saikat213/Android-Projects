package com.example.chatapplication.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import com.example.chatapplication.R
import com.example.chatapplication.model.Constants
import com.example.chatapplication.model.CustomSharedPreference
import com.example.chatapplication.model.User
import com.example.chatapplication.model.UserAuthService
import com.example.chatapplication.viewmodel.SharedViewModel
import com.example.chatapplication.viewmodel.SharedViewModelFactory
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class GetOtpFragment : Fragment() {
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var userPhnNumber : EditText
    private lateinit var getOTP : Button
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_get_otp, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity(), SharedViewModelFactory(
            UserAuthService()))[SharedViewModel::class.java]
        userPhnNumber = view.findViewById(R.id.phoneNumber)
        getOTP = view.findViewById(R.id.getOtp)
        progressBar = view.findViewById(R.id.progressBar1)
        getOtpVerification()
    }

    private fun getOtpVerification() {
        getOTP.setOnClickListener {
            val phoneNumber = userPhnNumber.text.toString()
            val user = User("User1", phoneNumber, "+91")
            progressBar.visibility = View.VISIBLE
            getOTP.visibility = View.INVISIBLE
            CustomSharedPreference.initSharedPreference(requireContext())
            CustomSharedPreference.addString(Constants.NEW_USER, "+91$phoneNumber")
            sendVerificationCode(user)
        }
    }

    private fun sendVerificationCode(user: User) {
        val phnAuth = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance()).setPhoneNumber("+91" + user.PhoneNumber)
            .setTimeout(60, TimeUnit.SECONDS).setActivity(requireActivity()).setCallbacks(object :
                PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                    progressBar.visibility = View.GONE
                    getOTP.visibility = View.VISIBLE
                }

                override fun onVerificationFailed(exception: FirebaseException) {
                    progressBar.visibility = View.GONE
                    getOTP.visibility = View.VISIBLE
                    Toast.makeText(requireContext(), exception.message, Toast.LENGTH_LONG).show()
                }

                override fun onCodeSent(verificationID: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken) {
                    progressBar.visibility = View.GONE
                    getOTP.visibility = View.VISIBLE
                    CustomSharedPreference.initSharedPreference(requireContext())
                    CustomSharedPreference.addString(Constants.VERIFICATION_ID, verificationID)
                    sharedViewModel.gotoVerifyOtpPageStatus(true)
                }
            }).build()
        PhoneAuthProvider.verifyPhoneNumber(phnAuth)
    }
}