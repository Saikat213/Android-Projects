package com.example.chatapplication.utility

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplication.model.AuthListener
import com.example.chatapplication.model.User
import com.example.chatapplication.view.ToastMessages
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage

class FirebaseService {
    private val db = FirebaseFirestore.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val userID = firebaseAuth.currentUser!!.uid
    private val storageReference = FirebaseStorage.getInstance().getReference("profilePictures")
    private var userImage : String = ""

    fun addUserInfoToDatabase(user : User, listener : (AuthListener) -> Unit) {
        db.collection("Users").document(userID).set(user).addOnCompleteListener {
            if (it.isSuccessful)
                listener(AuthListener(true, "Success"))
            else
                listener(AuthListener(false, "Failed"))
        }
    }

    fun uploadUserProfilePicture(imageUri: Uri, context: Context): String {
        if (imageUri != null) {
            val fileReference = storageReference.child("image/${userID}.jpg")
            fileReference.delete()
            fileReference.putFile(imageUri).addOnSuccessListener {
                if (it.task.isSuccessful) {
                    fileReference.downloadUrl.addOnSuccessListener {
                        userImage = it.toString()
                        Log.d("uri-->", userImage)
                    }.addOnFailureListener {
                        ToastMessages().displayMessage(context, it.message!!)
                    }
                }
            }.addOnFailureListener {
                ToastMessages().displayMessage(context, it.message!!)
            }
        }
        return userImage
    }

    fun getUserInfo(context: Context, recyclerView: RecyclerView) {
        val userDetails = ArrayList<User>()
        val myAdapter = UserAdapter(userDetails, context)
        db.collection("Users").addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.d("Firestore Error", error.message.toString())
                }
                for (documents: DocumentChange in value?.documentChanges!!) {
                    if (documents.type == DocumentChange.Type.ADDED) {
                        val data = documents.document.toObject(User::class.java)
                        userDetails.add(data)
                    }
                }
                myAdapter.notifyDataSetChanged()
                recyclerView.adapter = UserAdapter(userDetails, context)
            }
        })
    }
}

