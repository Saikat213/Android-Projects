package com.example.fundooapp

import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.fundooapp.model.NotesServiceImpl
import com.example.fundooapp.model.UserAuthService
import com.example.fundooapp.viewmodel.SharedViewModel
import com.example.fundooapp.viewmodel.SharedViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CreateNotesFragment : Fragment(R.layout.fragment_create_notes) {
    lateinit var notesTitle: EditText
    lateinit var notesContent: EditText
    lateinit var saveNotes: FloatingActionButton
    lateinit var sharedViewModel: SharedViewModel

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
        saveData()
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
            NotesServiceImpl().saveDataToFirestore(title, content, requireContext())
            sharedViewModel.gotoHomePage(true)
        }
    }
}