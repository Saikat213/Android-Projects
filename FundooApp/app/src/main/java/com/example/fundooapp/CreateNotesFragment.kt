package com.example.fundooapp

import android.app.Activity
import android.content.ContentValues.TAG
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fundooapp.model.NotesData
import com.example.fundooapp.model.UserAuthService
import com.example.fundooapp.viewmodel.NoteAdapter
import com.example.fundooapp.viewmodel.SharedViewModel
import com.example.fundooapp.viewmodel.SharedViewModelFactory
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import java.util.*
import kotlin.collections.ArrayList

class CreateNotesFragment : Fragment(R.layout.fragment_create_notes) {
    lateinit var createNotesToolbar: androidx.appcompat.widget.Toolbar
    lateinit var notesTitle: EditText
    lateinit var notesContent: EditText
    lateinit var saveNotes: FloatingActionButton
    lateinit var sharedViewModel: SharedViewModel
    var fstore: FirebaseFirestore = FirebaseFirestore.getInstance()
    lateinit var firebaseAuth: FirebaseAuth
    //lateinit var userID: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //createNotesToolbar = view.findViewById(R.id.createNotes_Toolbar)
        notesTitle = view.findViewById(R.id.notesTitle)
        notesContent = view.findViewById(R.id.notesDescription)
        saveNotes = view.findViewById(R.id.saveNotes)
        sharedViewModel = ViewModelProvider(
            requireActivity(), SharedViewModelFactory(
                UserAuthService()
            )
        )[SharedViewModel::class.java]
        firebaseAuth = FirebaseAuth.getInstance()
        //fstore = FirebaseFirestore.getInstance()
        //userID = firebaseAuth.currentUser!!.uid
        dataUploadToFirestore()
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }

    fun dataUploadToFirestore() {
        saveNotes.setOnClickListener {
            val title = notesTitle.text.toString()
            val content = notesContent.text.toString()
            var userID = FirebaseAuth.getInstance().currentUser!!.uid

            var documentReference =
                fstore.collection("notes").document(userID).collection("My notes").document()

            var note = mutableMapOf<String, String>()
            note.put("Title", title)
            note.put("Content", content)
            documentReference.set(note).addOnSuccessListener {
                Log.d(TAG, "Success user data created..")
                sharedViewModel.gotoHomePage(true)
                Toast.makeText(context, "SUccess", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Log.d(TAG, "Failed data creation")
                Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun updateNotes(noteData : NotesData) {
        var userID = FirebaseAuth.getInstance().currentUser!!.uid
        var documentReference =
            fstore.collection("notes").document(userID).collection("My notes").document(noteData.ID.toString())
        documentReference.update("Title", "Monday")
        documentReference.update("Content", "Aloo paratha")
    }
}