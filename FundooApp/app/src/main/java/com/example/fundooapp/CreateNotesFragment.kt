package com.example.fundooapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.example.fundooapp.model.NotesData
import com.example.fundooapp.model.NotesServiceImpl
import com.example.fundooapp.model.UserAuthService
import com.example.fundooapp.viewmodel.SharedViewModel
import com.example.fundooapp.viewmodel.SharedViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class CreateNotesFragment : Fragment(R.layout.fragment_create_notes) {
    lateinit var notesTitle : EditText
    lateinit var notesContent : EditText
    lateinit var saveNotes : FloatingActionButton
    lateinit var sharedViewModel : SharedViewModel
    lateinit var notesToolbar : Toolbar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notesTitle = view.findViewById(R.id.notesTitle)
        notesContent = view.findViewById(R.id.notesDescription)
        saveNotes = view.findViewById(R.id.saveNotes)
        notesToolbar = view.findViewById(R.id.createNotes_Toolbar)
        sharedViewModel = ViewModelProvider(
            requireActivity(), SharedViewModelFactory(
                UserAuthService()
            )
        )[SharedViewModel::class.java]
        saveData()
        createNotesToolbar(notesToolbar, requireContext())
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }

    private fun createNotesToolbar(toolbar: Toolbar, context: Context) {
        toolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.label -> {
                    Toast.makeText(context, "To be Implemented", Toast.LENGTH_SHORT).show()
                }
                R.id.Reminder -> {
                    addReminder()
                }
            }
            true
        }
    }

    private fun saveData() {
        saveNotes.setOnClickListener {
            val title = notesTitle.text.toString()
            val content = notesContent.text.toString()
            NotesServiceImpl().saveNotes(title, content, requireContext())
            sharedViewModel.gotoHomePage(true)
        }
    }

    fun addReminder() {
        val bundle = Bundle()
        bundle.putString("Title", notesTitle.text.toString())
        bundle.putString("Content", notesContent.text.toString())
        val reminderDialog = ReminderDialog()
        reminderDialog.arguments = bundle
        reminderDialog.show((activity as AppCompatActivity?)!!.supportFragmentManager, "Reminder")
    }
}