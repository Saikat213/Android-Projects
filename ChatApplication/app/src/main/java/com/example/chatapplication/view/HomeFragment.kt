package com.example.chatapplication.view

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplication.R
import com.example.chatapplication.model.CustomSharedPreference
import com.example.chatapplication.utility.UserAdapter
import com.example.chatapplication.model.User
import com.example.chatapplication.model.UserAuthService
import com.example.chatapplication.utility.FirebaseService
import com.example.chatapplication.viewmodel.SharedViewModel
import com.example.chatapplication.viewmodel.SharedViewModelFactory
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter : UserAdapter
    private lateinit var customToolbar : Toolbar
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = requireContext()
        sharedViewModel = ViewModelProvider(requireActivity(), SharedViewModelFactory(
            UserAuthService()))[SharedViewModel::class.java]
        customToolbar = view.findViewById(R.id.toolbar)
        recyclerView = view.findViewById(R.id.homeRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        userAdapter = UserAdapter(ArrayList<User>(), context)
        recyclerView.adapter = userAdapter
        FirebaseService().getUserInfo(requireContext(), recyclerView)
        toolbarMenu(customToolbar)
    }

    private fun toolbarMenu(toolbar : Toolbar) {
        val userDetails = FirebaseService().retrieveUserProfileDetails(requireContext())
        toolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.profile -> {
                    Log.d("UserData-->", "$userDetails")
                    if (userDetails != null) {
                        CustomSharedPreference.initSharedPreference(requireContext())
                        CustomSharedPreference.addString("UpdateName", userDetails[0].Name)
                        CustomSharedPreference.addString("UpdateImage", userDetails[0].ImageUri)
                        CustomSharedPreference.addString("UpdateNumber", userDetails[0].PhoneNumber)
                    }
                    sharedViewModel.gotoUpdateProfilePageStatus(true)
                }
                R.id.signout -> {
                }
            }
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.custom_toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}