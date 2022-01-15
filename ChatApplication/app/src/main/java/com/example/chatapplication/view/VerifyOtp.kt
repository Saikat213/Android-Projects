package com.example.chatapplication.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.chatapplication.R
import com.example.chatapplication.model.UserAuthService
import com.example.chatapplication.viewmodel.SharedViewModel
import com.example.chatapplication.viewmodel.SharedViewModelFactory
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
    private lateinit var displayNumber : TextView
    private lateinit var resendOtp : TextView
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_verify_otp, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle : Bundle
        var verificationId : String ?= null
        inputCode1 = view.findViewById(R.id.inputCode1)
        inputCode2 = view.findViewById(R.id.inputCode2)
        inputCode3 = view.findViewById(R.id.inputCode3)
        inputCode4 = view.findViewById(R.id.inputCode4)
        inputCode5 = view.findViewById(R.id.inputCode5)
        inputCode6 = view.findViewById(R.id.inputCode6)
        displayNumber = view.findViewById(R.id.mobileNumber)
        resendOtp = view.findViewById(R.id.resendOtp)
        sharedViewModel = ViewModelProvider(requireActivity(), SharedViewModelFactory(
            UserAuthService()))[SharedViewModel::class.java]
        bundle = this.requireArguments()
        if (bundle != null) {
            displayNumber.setText(bundle.getString("UserPhnNumber"))
            verificationId = bundle.getString("VerificationId")
        }
        setupOtpInputs()
        otpVerify(verificationId)
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

    fun otpVerify(verificationId : String?) {
        val otp = verifyValidOtp()
        val phoneAuthCredential : PhoneAuthCredential = PhoneAuthProvider.getCredential(verificationId!!, "432178")
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnSuccessListener {
            Toast.makeText(requireContext(), "Registration Success", Toast.LENGTH_SHORT).show()
            sharedViewModel.gotoHomePageStatus(true)
        }.addOnFailureListener {
            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
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