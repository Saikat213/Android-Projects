package com.example.fundooapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
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
        var context = requireContext()
        var toolbar = requireActivity().findViewById<Toolbar>(R.id.customToolbar)

        recyclerView.layoutManager = GridLayoutManager(requireContext(),2)
        var toggle = ActionBarDrawerToggle(requireActivity(), drawer, R.string.open, R.string.close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        //MainActivity().onNavigationItemSelected(drawer, navView, context)
        createNewNotes()
        MainActivity().toolbarIcon(context, toolbar)
        retrieveDatafromFirestore()
        changeLayoutView(toolbar, recyclerView, context)
    }

    private fun changeLayoutView(toolbar: Toolbar, recyclerView: RecyclerView, context : Context) {
        toolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.LinearView ->
                    recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                R.id.GridView ->
                    recyclerView.layoutManager = GridLayoutManager(context,2)
            }
            true
        }
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
                            val data = documents.document.toObject(NotesData::class.java)
                            data.ID = documents.document.id
                            Log.d("Firestore Data: ", documents.document.toString())
                            userNotes.add(data)
                        }
                    }
                    myAdapter.notifyDataSetChanged()
                    recyclerView.adapter = NoteAdapter(userNotes)
                }
            })
    }

    fun deleteNotes(id: String) {
        var userNotes = ArrayList<NotesData>()
        var myAdapter = NoteAdapter(userNotes)
        var db = FirebaseFirestore.getInstance()
        var userID = FirebaseAuth.getInstance().currentUser!!.uid
        Log.d("ID: ", id)
        //var builder: AlertDialog.Builder

        /*var builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Are you sure you want to delete?")
        builder.setPositiveButton("Yes") { DialogInterface, which ->*/
            db.collection("notes").document(userID).collection("My notes").document(id).delete()
            //myAdapter.notifyDataSetChanged()
            //startActivity(Intent(context, HomeFragment::class.java))
        /*}
        builder.setNegativeButton("No") { DialogInterface, which ->
        }
        var dialog = builder.create()
        dialog.show()*/
    }
}