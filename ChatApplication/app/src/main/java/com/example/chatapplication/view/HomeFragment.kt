package com.example.chatapplication.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplication.R
import com.example.chatapplication.utility.UserAdapter
import com.example.chatapplication.model.User
import com.example.chatapplication.model.UserAuthService
import com.example.chatapplication.utility.FirebaseService

class HomeFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter : UserAdapter
    private lateinit var customToolbar : Toolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = requireContext()
        customToolbar = view.findViewById(R.id.toolbar)
        recyclerView = view.findViewById(R.id.homeRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        userAdapter = UserAdapter(ArrayList<User>(), context)
        recyclerView.adapter = userAdapter
        FirebaseService().getUserInfo(requireContext(), recyclerView)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.custom_toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}