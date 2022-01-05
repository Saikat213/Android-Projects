package com.example.fundooapp

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fundooapp.model.NotesServiceImpl
import com.example.fundooapp.model.UserAuthService
import com.example.fundooapp.viewmodel.NoteAdapter
import com.example.fundooapp.viewmodel.SharedViewModel
import com.example.fundooapp.viewmodel.SharedViewModelFactory
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class HomeFragment : Fragment(R.layout.fragment_home) {
    lateinit var imageUri: Uri
    lateinit var fabButton: FloatingActionButton
    lateinit var bottomAppBar : BottomAppBar
    lateinit var sharedViewModel: SharedViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: NoteAdapter
    lateinit var searchData: SearchView
    lateinit var imageBtn : ImageView
    lateinit var userEmailID : TextView
    lateinit var storageReference : StorageReference
    lateinit var dbReference : DatabaseReference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = requireContext()
        val toolbar = requireActivity().findViewById<Toolbar>(R.id.customToolbar)
        sharedViewModel = ViewModelProvider(
            requireActivity(),
            SharedViewModelFactory(UserAuthService())
        )[SharedViewModel::class.java]
        fabButton = view.findViewById(R.id.FAV_addNote)
        bottomAppBar = view.findViewById(R.id.bottomAppBar)
        recyclerView = view.findViewById(R.id.recyclerViewLayout)
        searchData = activity?.findViewById(R.id.searchButton)!!
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        val getNotes = NotesServiceImpl().getDataFromFirestore(context, recyclerView)
        adapter = NoteAdapter(getNotes, context)
        recyclerView.adapter = adapter
        createNewNotes()
        appBarIcons(context, bottomAppBar)
        toolbarIcons(toolbar, recyclerView, context)
        filterData()
    }

    private fun appBarIcons(context: Context, bottomAppBar: BottomAppBar) {
        bottomAppBar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.paint -> Toast.makeText(context, "To be continued...", Toast.LENGTH_SHORT).show()
                R.id.microphone -> Toast.makeText(context, "Audio record", Toast.LENGTH_SHORT).show()
                R.id.photo -> Toast.makeText(context, "To be implemented..", Toast.LENGTH_SHORT).show()
            }
            true
        }
    }

    private fun toolbarIcons(toolbar: Toolbar, recyclerView: RecyclerView, context: Context) {
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.LinearView ->
                    recyclerView.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                R.id.GridView ->
                    recyclerView.layoutManager = GridLayoutManager(context, 2)
                R.id.UserProfile -> profileDialog(context)
            }
            true
        }
    }

    private fun createNewNotes() {
        fabButton.setOnClickListener {
            sharedViewModel.gotoCreateNotes(true)
        }
    }

    fun filterData() {
        searchData.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(text: String?): Boolean {
                adapter.filter.filter(text)
                return true
            }

            override fun onQueryTextChange(text: String?): Boolean {
                adapter.filter.filter(text)
                recyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
                return true
            }

        })
    }

    fun profileDialog(context: Context) {
        val firebaseAuth = FirebaseAuth.getInstance()
        val uid = firebaseAuth.currentUser!!
        storageReference = FirebaseStorage.getInstance().getReference("uploads")
        dbReference = FirebaseDatabase.getInstance().getReference("uploads")
        val builder = AlertDialog.Builder(context)
        val view = layoutInflater.inflate(R.layout.custom_dialog_layout, null)
        imageBtn = view.findViewById(R.id.profileImage)
        userEmailID = view.findViewById(R.id.profileEmailID)
        userEmailID.text = uid.email.toString()
        builder.setView(view)
        NotesServiceImpl().retrieveUserProfilePicture(context, storageReference, imageBtn)
        imageBtn.setOnClickListener {
            chooseImageFile()
        }
        builder.setMessage("Profile").setPositiveButton("SignOut", DialogInterface.OnClickListener { dialog, id ->
            firebaseAuth.signOut()
            startActivity(Intent(context, MainActivity::class.java))
        }).setNegativeButton("Change Profile Picture", DialogInterface.OnClickListener { dialog, id ->
            storageReference.child("image/${uid.uid}.jpg").delete()
            NotesServiceImpl().uploadUserProfilePicture(context, storageReference, dbReference, imageUri)
        })
        builder.create().show()
    }

    fun chooseImageFile() {
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(intent, image_request)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == image_request && resultCode == RESULT_OK && data != null && data.data != null) {
            imageUri = data.data!!
            imageBtn.setImageURI(imageUri)
        }
    }

    companion object {
      private const val image_request = 1
    }
}