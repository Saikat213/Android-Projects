package com.example.fundooapp

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.fundooapp.Pagination.Companion.PAGE_START
import com.example.fundooapp.model.NotesData
import com.example.fundooapp.model.NotesServiceImpl
import com.example.fundooapp.model.UserAuthService
import com.example.fundooapp.network.ApiInterface
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
import kotlinx.coroutines.Runnable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment(R.layout.fragment_home), SwipeRefreshLayout.OnRefreshListener {
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
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var expandableListView: ExpandableListView
    var getNotes: ArrayList<NotesData>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = requireContext()
        val toolbar = requireActivity().findViewById<Toolbar>(R.id.customToolbar)
        recyclerView = view.findViewById(R.id.recyclerViewLayout)
        getNotes = NotesServiceImpl().getUserNotes(context, recyclerView)
        sharedViewModel = ViewModelProvider(
            requireActivity(),
            SharedViewModelFactory(UserAuthService())
        )[SharedViewModel::class.java]
        fabButton = view.findViewById(R.id.FAV_addNote)
        bottomAppBar = view.findViewById(R.id.bottomAppBar)
        profilePicture = activity?.findViewById(R.id.profilePic)!!
        progressBar = view.findViewById(R.id.progressBar)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh)
        searchData = activity?.findViewById(R.id.searchButton)!!
       // expandableListView = view.findViewById(R.id.expandableList)
        swipeRefreshLayout.setOnRefreshListener(this)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = NoteAdapter(ArrayList<NotesData>(), context)
        recyclerView.adapter = adapter
        doApiCall(getNotes)
        recyclerView.addOnScrollListener(object : Pagination(GridLayoutManager(requireContext(), 2)) {
            override fun loadMoreItems() {
                isLoading = true
                currentPage++
                doApiCall(getNotes)
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun isLastPage(): Boolean {
                return isLastPage
            }
        })
        createNewNotes()
        appBarIcons(context, bottomAppBar)
        toolbarIcons(toolbar, recyclerView, context)
        filterData()
        getUserProfilePicture(context)
    }

    private fun doApiCall(getNotes: ArrayList<NotesData>?) {
        itemCount = 0
        val items = ArrayList<NotesData>()
        Handler().postDelayed(Runnable {
            for (i in 0..10) {
                if (getNotes?.size!! > 0 && itemCount < getNotes.size) {
                    items.add(getNotes[itemCount])
                    itemCount++
                }
            }
            if (currentPage != PAGE_START)
                adapter.removeLoading()
            adapter.addItems(items)
            swipeRefreshLayout.isRefreshing = false
            if (currentPage < pageLimit)
                adapter.addLoading()
            else
                isLastPage = true
            isLoading = false
        }, 1500)
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

    private fun profileDialog(context: Context) {
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

    private fun chooseImageFile() {
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

    override fun onRefresh() {
        itemCount = 0
        currentPage = PAGE_START
        isLastPage = false
        adapter.clearList()
        doApiCall(getNotes)
    }

    fun showList(context: Context) {
        LabelService().retrieveLabelCollection(context, expandableListView)
    }

    companion object {
        private const val image_request = 1
        private const val pageLimit = 10
        private var itemCount = 0
        private var currentPage = PAGE_START
        private var isLoading : Boolean = false
        private var isLastPage : Boolean = false
    }
}