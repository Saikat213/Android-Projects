package com.example.fundooapp

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fundooapp.model.User
import com.example.fundooapp.model.UserAuthService
import com.example.fundooapp.viewmodel.RegisterViewModel
import com.example.fundooapp.viewmodel.RegisterViewModelFactory
import com.example.fundooapp.viewmodel.SharedViewModel
import com.example.fundooapp.viewmodel.SharedViewModelFactory
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

class RegisterFragment : Fragment(R.layout.fragment_register) {
    lateinit var inputEmail: EditText
    lateinit var inputPassword: EditText
    lateinit var confirmPassword: EditText
    lateinit var registerViewModel: RegisterViewModel
    lateinit var sharedViewModel: SharedViewModel
    lateinit var signup : Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.fragment_login, container, false)

        registerViewModel =
            ViewModelProvider(this, RegisterViewModelFactory(UserAuthService())).get(
                RegisterViewModel::class.java
            )
        sharedViewModel = ViewModelProvider(requireActivity(),
            SharedViewModelFactory(UserAuthService())
        )[SharedViewModel::class.java]
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inputEmail = view.findViewById(R.id.email)!!
        inputPassword = view.findViewById(R.id.password)
        confirmPassword = view.findViewById(R.id.confirmPassword)
        signup = view.findViewById(R.id.signupButton)
        val loginAfterSignup = view.findViewById<TextView>(R.id.loginTextView)

        registerUser()
    }

    private fun registerUser() {
        signup.setOnClickListener {
            val email = inputEmail.text.toString()
            val pass = inputPassword.text.toString()
            val cnfPass = confirmPassword.text.toString()
            val user = User(emailID = email, password = pass, confirmPassword = cnfPass, imageURL = "")
            if (pass.isEmpty() || pass.length<6)
                inputPassword.setError("Enter valid password")
            else {
                registerViewModel.registerUser(user)
                registerViewModel.registerStatus.observe(viewLifecycleOwner, Observer {
                    if (it.status){
                        sharedViewModel.gotoHomePage(true)
                    } else {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                })
        }
        }
    }
}
