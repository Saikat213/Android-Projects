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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fundooapp.model.UserAuthService
import com.example.fundooapp.viewmodel.LoginViewModel
import com.example.fundooapp.viewmodel.LoginViewModelFactory
import com.example.fundooapp.viewmodel.SharedViewModel
import com.example.fundooapp.viewmodel.SharedViewModelFactory
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment(R.layout.fragment_login) {
    lateinit var inputEmail : EditText
    lateinit var inputPassword : EditText
    lateinit var firebaseAuth : FirebaseAuth
    lateinit var loginViewModel : LoginViewModel
    lateinit var sharedViewModel : SharedViewModel
    lateinit var createNewAccount : TextView
    lateinit var btnLogin : Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.fragment_login, container, false)
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory(UserAuthService()))
            .get(LoginViewModel::class.java)
        sharedViewModel = ViewModelProvider(requireActivity(), SharedViewModelFactory(UserAuthService()))[SharedViewModel::class.java]
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inputEmail = view.findViewById(R.id.emailAddress)
        inputPassword = view.findViewById(R.id.LoginPassword)
        createNewAccount = view.findViewById(R.id.createNewAccount)
        btnLogin = view.findViewById<Button>(R.id.loginButton)
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

    fun gotoRegisterPage() {
        createNewAccount.setOnClickListener {
            Toast.makeText(requireContext(), "CHeck", Toast.LENGTH_SHORT).show()
            sharedViewModel.gotoregistrationPage(true)
            //sharedViewModel.gotoHomePage(false)
        }
    }


    private fun navigateUserToHomeScreen() {
        var changeToActivity = Intent(this.context, HomeActivity::class.java)
        startActivity(changeToActivity)
    }
}