package com.example.fundooapp

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.fundooapp.model.NotesData
import com.example.fundooapp.model.UserAuthService
import com.example.fundooapp.viewmodel.NoteAdapter
import com.example.fundooapp.viewmodel.SharedViewModel
import com.example.fundooapp.viewmodel.SharedViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class HomeFragment : Fragment(R.layout.fragment_home) {
    lateinit var fabButton: FloatingActionButton
    private lateinit var sharedViewModel: SharedViewModel
    lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_home, container, false)
        sharedViewModel = ViewModelProvider(
            requireActivity(),
            SharedViewModelFactory(UserAuthService())
        )[SharedViewModel::class.java]
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fabButton = view.findViewById(R.id.FAV_addNote)
        recyclerView = view.findViewById(R.id.recyclerViewLayout)
        var drawer = requireActivity().findViewById<DrawerLayout>(R.id.drawerLayout)
        var navView = requireActivity().findViewById<NavigationView>(R.id.navigationView)
        recyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        var toggle = ActionBarDrawerToggle(requireActivity(), drawer, R.string.open, R.string.close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        var context = requireContext()
        MainActivity().onNavigationItemSelected(drawer, navView, context)
        createNewNotes()
        var toolbar = requireActivity().findViewById<Toolbar>(R.id.customToolbar)
        MainActivity().toolbarIcon(context, toolbar)
        retrieveDatafromFirestore()
    }

    private fun createNewNotes() {
        fabButton.setOnClickListener {
            sharedViewModel.gotoCreateNotes(true)
        }
    }

    /*override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        var itemID = menuItem.itemId
        when(itemID) {
            R.id.notes -> Toast.makeText(requireContext(), "Notes", Toast.LENGTH_SHORT).show()
            R.id.archive -> Toast.makeText(requireContext(), "Archive", Toast.LENGTH_SHORT).show()
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }*/

    fun retrieveDatafromFirestore() {
        var documentReference = FirebaseFirestore.getInstance()
        var userID = FirebaseAuth.getInstance().currentUser!!.uid
        var userNotes = ArrayList<NotesData>()
        var myAdapter = NoteAdapter(userNotes)
        documentReference.collection("notes").document(userID).collection("My notes")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.d("Firebase error: ", error.message.toString())
                        return
                    }
                    for (documents: DocumentChange in value?.documentChanges!!) {
                        if (documents.type == DocumentChange.Type.ADDED) {
                            Log.d("Firestore Data: ", documents.document.toString())
                            userNotes.add(documents.document.toObject(NotesData::class.java))
                        }
                    }
                    myAdapter.notifyDataSetChanged()
                    recyclerView.adapter = NoteAdapter(userNotes)
                }
            })
    }
}