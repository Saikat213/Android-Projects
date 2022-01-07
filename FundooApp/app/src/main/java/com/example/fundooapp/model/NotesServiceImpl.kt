package com.example.fundooapp.model

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.fundooapp.DatabaseHandler
import com.example.fundooapp.viewmodel.NoteAdapter
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.*
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class NotesServiceImpl {
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val userID = firebaseAuth.currentUser!!.uid

    fun getUserNotes(context: Context, recyclerView: RecyclerView): ArrayList<NotesData> {
        var userNotes = ArrayList<NotesData>()
        val myAdapter = NoteAdapter(userNotes, context)
        if (isOnline(context)) {
            val documentReference = FirebaseFirestore.getInstance()
            documentReference.collection("notes").document(userID).collection("My notes")
                .whereEqualTo("Archive", "false")
                .addSnapshotListener(object : EventListener<QuerySnapshot> {
                    override fun onEvent(
                        value: QuerySnapshot?,
                        error: FirebaseFirestoreException?
                    ) {
                        if (error != null) {
                            Log.d("Firebase error: ", error.message.toString())
                            return
                        }
                        for (documents: DocumentChange in value?.documentChanges!!) {
                            if (documents.type == DocumentChange.Type.ADDED) {
                                val data = documents.document.toObject(NotesData::class.java)
                                data.ID = documents.document.id
                                userNotes.add(data)
                            }
                        }
                        myAdapter.notifyDataSetChanged()
                        recyclerView.adapter = NoteAdapter(userNotes, context)
                    }
                })
        } else {
            val db = DatabaseHandler(context)
            val data = db.retriveData("false")
            while (data.moveToNext()) {
                userNotes.add(NotesData(data.getString(1), data.getString(2), data.getString(3)))
            }
            myAdapter.notifyDataSetChanged()
            recyclerView.adapter = NoteAdapter(userNotes, context)
        }
        return userNotes
    }

    fun saveNotes(title: String, content: String, context: Context) {
        val database = DatabaseHandler(context)
        if (isOnline(context)) {
            val fstore = FirebaseFirestore.getInstance()
            val documentReference =
                fstore.collection("notes").document(userID).collection("My notes").document()
            val note = mutableMapOf<String, String>()
            note.put("Title", title)
            note.put("Content", content)
            note.put("Archive", "false")
            documentReference.set(note).addOnSuccessListener {
                database.saveData(title, content, "false")
                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show()
            }
        } else {
            database.saveData(title, content, "false")
        }
    }

    fun deleteNotes(id: String?, title: String, context: Context) {
        val database = DatabaseHandler(context)
        if (isOnline(context)) {
            val firebaseStore = FirebaseFirestore.getInstance()
            firebaseStore.collection("notes").document(userID).collection("My notes").document(id!!)
                .delete().addOnSuccessListener {
                    database.deleteData(title)
                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Log.d("Deletion error: ", "$it")
                    Toast.makeText(context, "Deletion failed", Toast.LENGTH_SHORT).show()
                }
        } else {
            database.deleteData(title)
            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
        }
    }

    fun uploadUserProfilePicture(
        context: Context,
        storageReference: StorageReference,
        dbReference: DatabaseReference,
        imageUri: Uri
    ) {
        if (imageUri != null) {
            val fileReference: StorageReference =
                storageReference.child("image/${userID}.jpg")
            fileReference.putFile(imageUri).addOnSuccessListener {
                val uploadID = dbReference.push().key
                dbReference.child(uploadID!!)
                Toast.makeText(context, "Upload Success", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "No file selected", Toast.LENGTH_SHORT).show()
        }
    }

    fun retrieveUserProfilePicture(
        context: Context,
        storageReference: StorageReference,
        userImage: ImageView
    ) {
        storageReference.child("image/${userID}.jpg").downloadUrl.addOnSuccessListener {
            Picasso.with(context).load(it).into(userImage)
        }.addOnFailureListener {
            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun addUserDetails(imageUrl: String, context: Context) {
        val firebaseStore = FirebaseFirestore.getInstance()
        val userReference = firebaseStore.collection("notes").document(userID)

        val userDetails = mutableMapOf<String, String>()
        userDetails.put("Email", firebaseAuth.currentUser!!.email.toString())
        userDetails.put("ImageUrl", imageUrl)

        userReference.set(userDetails).addOnSuccessListener {
            Toast.makeText(context, "User Details Uploaded", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet----->>>>", "${NetworkCapabilities.TRANSPORT_CELLULAR}")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)){
                    Log.d("Wifi---->>>>", "${NetworkCapabilities.TRANSPORT_WIFI}")
                    return true
                }
            }
        }
        return false
    }
}