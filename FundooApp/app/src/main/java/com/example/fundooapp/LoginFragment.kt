package com.example.fundooapp

import android.content.ContentValues.TAG
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.fragment_login, container, false)
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
        firebaseAuth = FirebaseAuth.getInstance()

        gotoRegisterPage()
        loginUser()
    }

    private fun loginUser() {
        btnLogin.setOnClickListener {
            var emailID = inputEmail.text.toString()
            var password = inputPassword.text.toString()
            if (password.isEmpty() || password.length < 6)
                inputPassword.setError("Enter valid password")
            loginViewModel.loginToFundoo(emailID, password)
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