package com.example.fundooapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

class RegisterFragment : Fragment(R.layout.fragment_register) {
    lateinit var inputEmail : EditText
    lateinit var inputPassword : EditText
    lateinit var confirmPassword : EditText
    lateinit var auth : FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inputEmail = view.findViewById<EditText>(R.id.email)
        inputPassword = view.findViewById<EditText>(R.id.password)
        confirmPassword = view.findViewById<EditText>(R.id.confirmPassword)
        val signup = view.findViewById<Button>(R.id.signupButton)
        auth = FirebaseAuth.getInstance()

        signup.setOnClickListener {
            val email = inputEmail.text.toString()
            val pass = inputPassword.text.toString()
            val cnfPass = confirmPassword.text.toString()
            performAuth(email, pass, cnfPass)
        }
    }

    private fun performAuth(email : String, passwd : String, cnfPasswd : String) {
        //val emailPattern : String = "^[a-z]{1}[a-z0-9+-._]+@[a-z.]+\\\\.[a-z]{2,4}"
        //if (!email.matches(emailPattern.toRegex()))
            //inputEmail.setError("Enter valid email")
        if (passwd.isEmpty() || passwd.length<6)
            inputPassword.setError("Enter valid password")
        else if (!passwd.equals(cnfPasswd))
            confirmPassword.setError("Password does not match")
        else {
            auth.createUserWithEmailAndPassword(email,passwd).addOnCompleteListener(OnCompleteListener {
                task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "SignUp Failed", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
