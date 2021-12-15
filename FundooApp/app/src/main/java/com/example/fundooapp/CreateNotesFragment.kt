package com.example.fundooapp

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.ViewModelProvider
import com.example.fundooapp.model.NotesData
import com.example.fundooapp.model.UserAuthService
import com.example.fundooapp.viewmodel.SharedViewModel
import com.example.fundooapp.viewmodel.SharedViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class CreateNotesFragment : Fragment(R.layout.fragment_create_notes) {
    lateinit var notesTitle: EditText
    lateinit var notesContent: EditText
    lateinit var saveNotes: FloatingActionButton
    lateinit var sharedViewModel: SharedViewModel
    var fstore: FirebaseFirestore = FirebaseFirestore.getInstance()
    lateinit var firebaseAuth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notesTitle = view.findViewById(R.id.notesTitle)
        notesContent = view.findViewById(R.id.notesDescription)
        saveNotes = view.findViewById(R.id.saveNotes)
        sharedViewModel = ViewModelProvider(
            requireActivity(), SharedViewModelFactory(
                UserAuthService()
            )
        )[SharedViewModel::class.java]
        firebaseAuth = FirebaseAuth.getInstance()
        saveData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }

    fun saveData() {
        saveNotes.setOnClickListener {
            val title = notesTitle.text.toString()
            val content = notesContent.text.toString()
            var userID = FirebaseAuth.getInstance().currentUser!!.uid
            //val db = DatabaseHandler(requireContext())

            var documentReference =
                fstore.collection("notes").document(userID).collection("My notes").document()

            var note = mutableMapOf<String, String>()
            note.put("Title", title)
            note.put("Content", content)
            documentReference.set(note).addOnSuccessListener {
                //db.saveData()
                Log.d(TAG, "Success user data created..")
                sharedViewModel.gotoHomePage(true)
                Toast.makeText(context, "SUccess", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Log.d(TAG, "Failed data creation")
                Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show()
            }
        }
    }
}