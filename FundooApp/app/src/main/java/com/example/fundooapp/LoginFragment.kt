package com.example.fundooapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.widget.*
import android.view.View
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment(R.layout.fragment_login) {
    lateinit var inputEmail : EditText
    lateinit var inputPassword : EditText
    lateinit var auth : FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inputEmail = view.findViewById(R.id.emailAddress)
        inputPassword = view.findViewById(R.id.LoginPassword)
        val btnLogin = view.findViewById<Button>(R.id.loginButton)
        auth = FirebaseAuth.getInstance()

        btnLogin.setOnClickListener {
            var email = inputEmail.text.toString()
            var passwd = inputPassword.text.toString()
            loginUser(email, passwd)
        }
    }

    private fun loginUser(email : String, password : String) {
        if (password.isEmpty() || password.length<6)
            inputPassword.setError("Enter valid password")

        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(OnCompleteListener {
            task ->
            if (task.isSuccessful) {
                Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_SHORT).show()
                sendUserToHomeActivity()
            } else {
                Toast.makeText(requireContext(), "Login Failed.. Try Again", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun sendUserToHomeActivity() {
        var changeToActivity = Intent(this.context, HomeActivity::class.java)
        startActivity(changeToActivity)
    }
}