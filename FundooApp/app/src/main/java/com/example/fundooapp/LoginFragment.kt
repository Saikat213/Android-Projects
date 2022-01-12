package com.example.fundooapp

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.widget.*
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fundooapp.model.UserAuthService
import com.example.fundooapp.viewmodel.*
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var inputEmail: EditText
    private lateinit var inputPassword: EditText
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var createNewAccount: TextView
    private lateinit var btnLogin: Button
    private lateinit var forgotPassword : TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_login, container, false)
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory(UserAuthService()))
            .get(LoginViewModel::class.java)
        sharedViewModel = ViewModelProvider(
            requireActivity(),
            SharedViewModelFactory(UserAuthService())
        )[SharedViewModel::class.java]
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inputEmail = view.findViewById(R.id.login_emailID)
        inputPassword = view.findViewById(R.id.LoginPassword)
        createNewAccount = view.findViewById(R.id.createNewAccount)
        btnLogin = view.findViewById(R.id.loginButton)
        forgotPassword = view.findViewById(R.id.forgotPassword)
        firebaseAuth = FirebaseAuth.getInstance()

        gotoRegisterPage()
        loginUser()
        resetPassword(requireContext())
    }

    private fun resetPassword(context: Context) {
        forgotPassword.setOnClickListener {
            val email = EditText(it.context)
            val resetDialog = AlertDialog.Builder(context)
            resetDialog.setView(email)
            resetDialog.setTitle("Reset Password").setMessage("Enter email to reset password:").
                setPositiveButton("Confirm", DialogInterface.OnClickListener { dialogInterface, i ->
                    val resetEmail = email.text.toString()
                    firebaseAuth.sendPasswordResetEmail(resetEmail).addOnSuccessListener {
                        Toast.makeText(context, "Link Sent Successfully", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    }
                }).setNegativeButton("Cancel", DialogInterface.OnClickListener { dialogInterface, i ->
            })
            resetDialog.create().show()
        }
    }

    private fun loginUser() {
        btnLogin.setOnClickListener {
            val emailID = inputEmail.text.toString()
            val password = inputPassword.text.toString()
            if (password.isEmpty() || password.length < 6)
                inputPassword.error = "Enter valid password"
            loginViewModel.loginToFundoo(emailID, password)
            //loginViewModel.loginWithApi(emailID, password)
            loginViewModel.loginStatus.observe(viewLifecycleOwner, Observer {
                Log.d(TAG, "loginUser: status ${it.status}")
                if (it.status) {
                    sharedViewModel.gotoHomePage(true)
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun gotoRegisterPage() {
        createNewAccount.setOnClickListener {
            sharedViewModel.gotoRegistrationPage(true)
        }
    }
}