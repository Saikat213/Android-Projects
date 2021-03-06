package com.example.fundooapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fundooapp.model.NotesData
import com.example.fundooapp.model.NotesServiceImpl
import com.example.fundooapp.viewmodel.NoteAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class ArchiveFragment : Fragment(R.layout.fragment_archive) {
    lateinit var archiveRecyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = requireContext()
        archiveRecyclerView = view.findViewById(R.id.archiveRecyclerView)
        archiveRecyclerView.layoutManager = GridLayoutManager(context, 2)
        retriveArchiveNotes(context)
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }

    fun retriveArchiveNotes(context: Context) {
        val archiveNotes = ArrayList<NotesData>()
        val myAdapter = NoteAdapter(archiveNotes, context)
        val db = DatabaseHandler(context)
        if (NotesServiceImpl().isOnline(context)) {
            val documentReference = FirebaseFirestore.getInstance()
            val userID = FirebaseAuth.getInstance().currentUser!!.uid
            documentReference.collection("Users").document(userID).collection("notes")
                .whereEqualTo("Archive", "true")
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
                                archiveNotes.add(data)
                            }
                        }
                        myAdapter.notifyDataSetChanged()
                        archiveRecyclerView.adapter = NoteAdapter(archiveNotes, context)
                    }
                })
        } else {
            val archiveData = db.retriveData("true")
            while (archiveData.moveToNext()) {
                archiveNotes.add(NotesData(archiveData.getString(1), archiveData.getString(2),
                                    archiveData.getString(3)))
            }
            myAdapter.notifyDataSetChanged()
            archiveRecyclerView.adapter = NoteAdapter(archiveNotes, context)
        }
    }
}