package com.example.fundooapp.model

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.fundooapp.DatabaseHandler
import com.example.fundooapp.viewmodel.NoteAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.*
import com.google.firebase.storage.StorageReference

class NotesServiceImpl {
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val userID = firebaseAuth.currentUser!!.uid

    fun getDataFromFirestore(context: Context, recyclerView: RecyclerView): ArrayList<NotesData> {
        val documentReference = FirebaseFirestore.getInstance()
        var userNotes = ArrayList<NotesData>()
        val myAdapter = NoteAdapter(userNotes, context)
        documentReference.collection("notes").document(userID).collection("My notes")
            .whereEqualTo("Archive", "false")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.d("Firebase error: ", error.message.toString())
                        return
                    }
                    for (documents: DocumentChange in value?.documentChanges!!) {
                        if (documents.type == DocumentChange.Type.ADDED) {
                            val data = documents.document.toObject(NotesData::class.java)
                            data.ID = documents.document.id
                            Log.d("Firestore Data: ", documents.document.toString())
                            userNotes.add(data)
                        }
                    }
                    myAdapter.notifyDataSetChanged()
                    recyclerView.adapter = NoteAdapter(userNotes, context)
                }
            })
        return userNotes
    }

    fun saveDataToFirestore(title: String, content: String, context: Context) {
        val fstore = FirebaseFirestore.getInstance()
        val database = DatabaseHandler(context)
        var documentReference =
            fstore.collection("notes").document(userID).collection("My notes").document()

        var note = mutableMapOf<String, String>()
        note.put("Title", title)
        note.put("Content", content)
        note.put("Archive", "false")
        documentReference.set(note).addOnSuccessListener {
            //database.saveData(NotesData(title, content, "", false))
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show()
        }
    }

    fun deleteNotes(id: String, context: Context) {
        val firebaseStore = FirebaseFirestore.getInstance()
        val database = DatabaseHandler(context)
        firebaseStore.collection("notes").document(userID).collection("My notes").document(id)
            .delete().addOnSuccessListener {
                //database.deleteData(id)
            }.addOnFailureListener {
                Log.d("Deletion error: ", "$it")
                Toast.makeText(context, "Deletion failed", Toast.LENGTH_SHORT).show()
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
                storageReference.child("image/${System.currentTimeMillis()}.jpg")
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
        storageReference.child("image").downloadUrl.addOnSuccessListener {
            userImage.setImageURI(it)
        }.addOnFailureListener {
            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
        }
    }
}