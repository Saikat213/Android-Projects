package com.example.fundooapp

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.fundooapp.model.NotesServiceImpl
import com.example.fundooapp.model.UserAuthService
import com.example.fundooapp.viewmodel.SharedViewModel
import com.example.fundooapp.viewmodel.SharedViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UpdateFragment : Fragment(R.layout.fragment_update) {
    lateinit var updateTitle: EditText
    lateinit var updateContent: EditText
    lateinit var updateFab: FloatingActionButton
    lateinit var sharedViewModel: SharedViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = requireContext()
        var bundle: Bundle
        updateTitle = view.findViewById(R.id.updateTitle)
        updateContent = view.findViewById(R.id.updateContent)
        updateFab = view.findViewById(R.id.updateNotes)
        sharedViewModel = ViewModelProvider(
            requireActivity(), SharedViewModelFactory(
                UserAuthService()
            )
        )[SharedViewModel::class.java]
        bundle = this.requireArguments()
        if (bundle != null) {
            updateTitle.setText(bundle.getString("Title"))
            updateContent.setText(bundle.getString("Content"))
            updateNotes(bundle.getString("ID"), bundle.getString("Title"), context)
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }

    fun updateNotes(id: String?, searchData : String?, context: Context) {
        val database = DatabaseHandler(context)
        updateFab.setOnClickListener {
            val newTitle = updateTitle.text.toString()
            val newContent = updateContent.text.toString()
            if (NotesServiceImpl().isOnline(context)) {
                val fstore: FirebaseFirestore = FirebaseFirestore.getInstance()
                val userID = FirebaseAuth.getInstance().currentUser!!.uid
                val documentReference =
                    fstore.collection("notes").document(userID).collection("My notes")
                        .document(id!!)
                documentReference.update("Title", newTitle)
                documentReference.update("Content", newContent)
                    .addOnSuccessListener {
                        database.updateData(newTitle, newContent, searchData!!)
                        Toast.makeText(context, "Update Success", Toast.LENGTH_SHORT).show()
                        sharedViewModel.gotoHomePage(true)
                    }.addOnFailureListener {
                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                    }
            } else {
                database.updateData(newTitle, newContent, searchData!!)
                Toast.makeText(context, "Update Success", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun updateArchiveField(id: String?, title : String?, context: Context) {
        val database = DatabaseHandler(context)
        if (NotesServiceImpl().isOnline(context)) {
            val fstore: FirebaseFirestore = FirebaseFirestore.getInstance()
            val userID = FirebaseAuth.getInstance().currentUser!!.uid
            val documentReference =
                fstore.collection("notes").document(userID).collection("My notes").document(id!!)
            documentReference.update("Archive", "true").addOnSuccessListener {
                database.updateArchive(title!!)
                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
        } else {
            database.updateArchive(title!!)
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        }
    }
}