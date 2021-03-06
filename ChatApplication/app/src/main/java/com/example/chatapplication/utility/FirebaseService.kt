package com.example.chatapplication.utility

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplication.model.AuthListener
import com.example.chatapplication.model.CustomSharedPreference
import com.example.chatapplication.model.User
import com.example.chatapplication.model.UserMessages
import com.example.chatapplication.view.ToastMessages
import com.example.chatapplication.viewmodel.SharedViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class FirebaseService {
    private val db : FirebaseFirestore
    private val firebaseAuth : FirebaseAuth
    private val userID : String
    private val storageReference : StorageReference
    private var senderRoom : String
    private var receiverRoom : String
    private val chatReference : DocumentReference

    init {
        firebaseAuth = FirebaseAuth.getInstance()
        senderRoom = firebaseAuth.currentUser!!.uid + CustomSharedPreference.get("ReceiverID")
        receiverRoom = CustomSharedPreference.get("ReceiverID") + firebaseAuth.currentUser!!.uid
        storageReference = FirebaseStorage.getInstance().getReference("profilePictures")
        userID = firebaseAuth.currentUser!!.uid
        db = FirebaseFirestore.getInstance()
        chatReference = db.collection("Chats").document(senderRoom)
    }

    fun addUserInfoToDatabase(user : User, listener : (AuthListener) -> Unit) {
        val userDetails = mutableMapOf<String, String>()
        userDetails.put("Name", user.Name)
        userDetails.put("PhoneNumber", user.PhoneNumber)
        userDetails.put("CountryCode", user.CountryCode)
        userDetails.put("ID", userID)
        val fileReference = storageReference.child("image/${userID}.jpg")
        fileReference.downloadUrl.addOnSuccessListener {
            userDetails.put("ImageUri", it.toString())
            db.collection("Users").document(userID).set(userDetails).addOnCompleteListener {
                if (it.isSuccessful)
                    listener(AuthListener(true, "Success"))
                else
                    listener(AuthListener(false, "Failed"))
            }
        }
    }

    fun uploadUserProfilePicture(imageUri: Uri, context: Context) {
        if (imageUri != null) {
            val fileReference = storageReference.child("image/${userID}.jpg")
            fileReference.delete()
            fileReference.putFile(imageUri).addOnSuccessListener {
                ToastMessages().displayMessage(context, "Success")
            }
        }
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
                        if ("+91" + data.PhoneNumber != firebaseAuth.currentUser!!.phoneNumber)
                            userDetails.add(data)
                    }
                }
                myAdapter.notifyDataSetChanged()
                recyclerView.adapter = UserAdapter(userDetails, context)
            }
        })
    }

    fun uploadUserChats(message : UserMessages, listener: (AuthListener) -> Unit) {
        db.collection("Chats").document(senderRoom).collection("Messages")
            .document().set(message).addOnCompleteListener {
                db.collection("Chats").document(receiverRoom).collection("Messages")
                    .document().set(message).addOnCompleteListener {
                        if (it.isSuccessful)
                            listener(AuthListener(true, "Upload Success"))
                        else
                            listener(AuthListener(false, "Failed"))
                    }
            }
    }

    fun retrieveUserChats(context: Context, recyclerView: RecyclerView) {
        val chatMessages = ArrayList<UserMessages>()
        val myadapter = MessageAdapter(chatMessages, context)
        chatReference.collection("Messages").orderBy("timeStamp")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.d("MessageError-->", error.message.toString())
                }
                for (documents : DocumentChange in value?.documentChanges!!) {
                    if (documents.type == DocumentChange.Type.ADDED) {
                        val chats = documents.document.toObject(UserMessages::class.java)
                        chatMessages.add(chats)
                    }
                }
                myadapter.notifyDataSetChanged()
                recyclerView.adapter = MessageAdapter(chatMessages, context)
            }
        })
    }

    fun retrieveUserProfileDetails(context: Context) : ArrayList<User> {
        var userdata = ArrayList<User>()
        db.collection("Users").addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.d("Firestore Error", error.message.toString())
                }
                for (documents: DocumentChange in value?.documentChanges!!) {
                    if (documents.type == DocumentChange.Type.ADDED) {
                        val data = documents.document.toObject(User::class.java)
                        if ("+91" + data.PhoneNumber == firebaseAuth.currentUser!!.phoneNumber){
                            userdata.add(data)
                        }
                    }
                }
            }
        })
        return userdata
    }

    fun updateUserProfile(user : User, listener : (AuthListener) -> Unit) {
        val documentReference = db.collection("Users").document(userID)
        val fileReference = storageReference.child("image/${userID}.jpg")
        documentReference.update("Name", user.Name)
        fileReference.downloadUrl.addOnSuccessListener {
            documentReference.update("ImageUri", it.toString()).addOnCompleteListener {
                if (it.isSuccessful)
                    listener(AuthListener(true, "Update Success"))
                else
                    listener(AuthListener(true, "Failed"))
            }
        }
    }

    fun retrieveUserProfilePicture(context: Context, userImage : ImageView) {
        storageReference.child("image/${userID}.jpg").downloadUrl.addOnSuccessListener {
            Picasso.with(context).load(it).into(userImage)
        }.addOnFailureListener {
            ToastMessages().displayMessage(context, it.message.toString())
        }
    }

    fun signoutUser(sharedViewModel: SharedViewModel) {
        firebaseAuth.signOut()
        sharedViewModel.gotoGetOtpPageStatus(true)
    }
}

