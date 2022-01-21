package com.example.chatapplication.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.chatapplication.R
import com.example.chatapplication.model.Constants
import com.example.chatapplication.model.CustomSharedPreference
import com.example.chatapplication.model.UserAuthService
import com.example.chatapplication.viewmodel.SharedViewModel
import com.example.chatapplication.viewmodel.SharedViewModelFactory
import com.example.chatapplication.viewmodel.VerifyOtpViewModel
import com.example.chatapplication.viewmodel.VerifyOtpViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class VerifyOtp : Fragment() {
    private lateinit var inputCode1 : EditText
    private lateinit var inputCode2 : EditText
    private lateinit var inputCode3 : EditText
    private lateinit var inputCode4 : EditText
    private lateinit var inputCode5 : EditText
    private lateinit var inputCode6 : EditText
    private lateinit var resendOtp : TextView
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var verifyOtp : Button
    private lateinit var progressBar : ProgressBar
    private lateinit var verifyOtpViewModel : VerifyOtpViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_verify_otp, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var verificationId : String ?= null
        var currentUserNumber : String ?= null
        var newUser : String ?= null
        inputCode1 = view.findViewById(R.id.inputCode1)
        inputCode2 = view.findViewById(R.id.inputCode2)
        inputCode3 = view.findViewById(R.id.inputCode3)
        inputCode4 = view.findViewById(R.id.inputCode4)
        inputCode5 = view.findViewById(R.id.inputCode5)
        inputCode6 = view.findViewById(R.id.inputCode6)
        resendOtp = view.findViewById(R.id.resendOtp)
        verifyOtp = view.findViewById(R.id.verifyOtp)
        progressBar = view.findViewById(R.id.progressBar2)
        sharedViewModel = ViewModelProvider(requireActivity(), SharedViewModelFactory(
            UserAuthService()))[SharedViewModel::class.java]
        verifyOtpViewModel = ViewModelProvider(this, VerifyOtpViewModelFactory(
            UserAuthService()))[VerifyOtpViewModel::class.java]
        verificationId = CustomSharedPreference.get(Constants.VERIFICATION_ID)
        currentUserNumber = CustomSharedPreference.get(Constants.EXISTING_USER)
        newUser = CustomSharedPreference.get(Constants.NEW_USER)
        Log.d(currentUserNumber, newUser!!)
        setupOtpInputs()
        otpVerify(verificationId, currentUserNumber, newUser)
    }

    private fun setupOtpInputs() {
        inputCode1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!p0.toString().trim().isEmpty())
                    inputCode2.requestFocus()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        inputCode2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!p0.toString().trim().isEmpty())
                    inputCode3.requestFocus()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        inputCode3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!p0.toString().trim().isEmpty())
                    inputCode4.requestFocus()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        inputCode4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!p0.toString().trim().isEmpty())
                    inputCode5.requestFocus()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        inputCode5.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!p0.toString().trim().isEmpty())
                    inputCode6.requestFocus()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun otpVerify(verificationId : String?, currentUser : String?, newUser : String?) {
        verifyOtp.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            verifyOtp.visibility = View.INVISIBLE
            val otp = verifyValidOtp()
            val phoneAuthCredential : PhoneAuthCredential = PhoneAuthProvider.getCredential(verificationId!!, otp!!)
            verifyOtpViewModel.signInWithCredential(phoneAuthCredential)
            verifyOtpViewModel.signInWithCredential.observe(viewLifecycleOwner, Observer {
                if (it.status) {
                    if (currentUser == newUser) {
                        sharedViewModel.gotoHomePageStatus(true)
                    } else {
                        sharedViewModel.gotoRegisterPageStatus(true)
                    }
                } else {
                    progressBar.visibility = View.GONE
                    verifyOtp.visibility = View.VISIBLE
                }
            })
        }
    }

    private fun verifyValidOtp() : String? {
        if (inputCode1.text.toString().trim().isEmpty() || inputCode2.text.toString().trim().isEmpty()
            || inputCode3.text.toString().trim().isEmpty() || inputCode4.text.toString().trim().isEmpty()
            || inputCode5.text.toString().trim().isEmpty() || inputCode6.text.toString().trim().isEmpty()) {
            Toast.makeText(requireContext(), "Enter Valid Otp", Toast.LENGTH_SHORT).show()
        } else {
            val code = inputCode1.text.toString() + inputCode2.text.toString() +
                    inputCode3.text.toString() + inputCode4.text.toString() +
                    inputCode5.text.toString() + inputCode6.text.toString()
            return code
        }
        return null
    }
}