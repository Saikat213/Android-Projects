package com.example.fundooapp

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
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

        updateTitle = view.findViewById(R.id.updateTitle)
        updateContent = view.findViewById(R.id.updateContent)
        updateFab = view.findViewById(R.id.updateNotes)
        sharedViewModel = ViewModelProvider(
            requireActivity(), SharedViewModelFactory(
                UserAuthService()
            )
        )[SharedViewModel::class.java]
        var bundle: Bundle
        bundle = this.requireArguments()
        if (bundle != null) {
            updateTitle.setText(bundle.getString("Title"))
            updateContent.setText(bundle.getString("Content"))
            updateNotes(bundle.getString("ID"))
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

    fun updateNotes(id: String?) {
        updateFab.setOnClickListener {
            val newTitle = updateTitle.text.toString()
            val newContent = updateContent.text.toString()
            var fstore: FirebaseFirestore = FirebaseFirestore.getInstance()
            Log.d("ID: ", id!!)
            var userID = FirebaseAuth.getInstance().currentUser!!.uid
            var documentReference =
                fstore.collection("notes").document(userID).collection("My notes").document(id!!)
            //val db = DatabaseHandler(requireContext())
            documentReference.update("Title", newTitle)
            documentReference.update("Content", newContent)
                .addOnSuccessListener {
                    //db.updateData(newTitle, newContent, id)
                    sharedViewModel.gotoHomePage(true)
                }.addOnFailureListener {
                    Log.d(ContentValues.TAG, "Update failed")
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                }
        }
    }
}