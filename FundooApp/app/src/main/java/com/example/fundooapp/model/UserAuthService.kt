package com.example.fundooapp.model

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

class UserAuthService {
    lateinit var firebaseAuth: FirebaseAuth

    init {
        firebaseAuth = FirebaseAuth.getInstance()
    }

    fun loginUser(emailID: String, password: String, listener: (AuthListener) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(emailID, password).addOnCompleteListener(
            OnCompleteListener {
                if (it.isSuccessful) {
                    listener(AuthListener(status = it.isSuccessful, "login Successful"))
                }
            })
    }

    fun registerUser(user: User, listener: (AuthListener) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(user.emailID, user.password).addOnCompleteListener {
            if (it.isSuccessful) {
                listener(AuthListener(status = it.isSuccessful, "Registered Successfully"))
            }
        }
    }
}