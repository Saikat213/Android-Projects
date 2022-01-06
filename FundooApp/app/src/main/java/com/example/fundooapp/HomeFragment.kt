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
import de.hdodenhof.circleimageview.CircleImageView

class HomeFragment : Fragment(R.layout.fragment_home) {
    lateinit var imageUri: Uri
    lateinit var fabButton: FloatingActionButton
    lateinit var bottomAppBar : BottomAppBar
    lateinit var sharedViewModel: SharedViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: NoteAdapter
    lateinit var searchData: SearchView
    lateinit var profileImage : ImageView
    lateinit var profilePicture : CircleImageView
    lateinit var userEmailID : TextView
    lateinit var storageReference : StorageReference
    lateinit var dbReference : DatabaseReference
    lateinit var progressBar : ProgressBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = requireContext()
        val toolbar = requireActivity().findViewById<Toolbar>(R.id.customToolbar)
        recyclerView = view.findViewById(R.id.recyclerViewLayout)
        var getNotes = NotesServiceImpl().getDataFromFirestore(context, recyclerView)
        sharedViewModel = ViewModelProvider(
            requireActivity(),
            SharedViewModelFactory(UserAuthService())
        )[SharedViewModel::class.java]
        fabButton = view.findViewById(R.id.FAV_addNote)
        bottomAppBar = view.findViewById(R.id.bottomAppBar)
        profilePicture = activity?.findViewById(R.id.profilePic)!!
        progressBar = view.findViewById(R.id.progressBar)
        searchData = activity?.findViewById(R.id.searchButton)!!
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = NoteAdapter(getNotes, context)
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                    isScrolling = true
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val manager = GridLayoutManager(requireContext(), 2)
                var currentItems = manager.childCount
                var totalItems = manager.itemCount
                var scrolledOutItems = manager.findFirstVisibleItemPosition()
                if (isScrolling && (currentItems + scrolledOutItems == totalItems)) {
                    isScrolling = false
                    progressBar.visibility = View.VISIBLE
                    getNotes = NotesServiceImpl().getDataFromFirestore(context, recyclerView)
                    adapter.notifyDataSetChanged()
                    progressBar.visibility = View.GONE

                }
            }
        })
        createNewNotes()
        appBarIcons(context, bottomAppBar)
        toolbarIcons(toolbar, recyclerView, context)
        filterData()
        getUserProfilePicture(context)
    }

    private fun getUserProfilePicture(context: Context) {
        storageReference = FirebaseStorage.getInstance().getReference("uploads")
        NotesServiceImpl().retrieveUserProfilePicture(context, storageReference, profilePicture)
        profilePicture.setOnClickListener {
            Toast.makeText(context, "profile", Toast.LENGTH_SHORT).show()
            profileDialog(context)
        }
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
        profileImage = view.findViewById(R.id.profileImage)
        userEmailID = view.findViewById(R.id.profileEmailID)
        userEmailID.text = uid.email.toString()
        NotesServiceImpl().retrieveUserProfilePicture(context, storageReference, profileImage)
        builder.setView(view)
        profileImage.setOnClickListener {
            chooseImageFile()
        }
        builder.setMessage("Profile")
            .setPositiveButton("SignOut", DialogInterface.OnClickListener { dialog, id ->
                firebaseAuth.signOut()
                startActivity(Intent(context, MainActivity::class.java))
            }).setNegativeButton(
            "Change Profile Picture",
            DialogInterface.OnClickListener { dialog, id ->
                storageReference.child("image/${uid.uid}.jpg").delete()
                NotesServiceImpl().uploadUserProfilePicture(
                    context,
                    storageReference,
                    dbReference,
                    imageUri
                )
                NotesServiceImpl().retrieveUserProfilePicture(context, storageReference, profilePicture)
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
            profileImage.setImageURI(imageUri)
        }
    }

    companion object {
        private const val image_request = 1
        private const val pageLimit = 10
        private var isScrolling : Boolean = false
    }
}