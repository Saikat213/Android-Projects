package com.example.fundooapp

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.SupportActionModeWrapper
import androidx.appcompat.view.SupportMenuInflater
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import com.example.fundooapp.model.UserAuthService
import com.example.fundooapp.viewmodel.LoginViewModel
import com.example.fundooapp.viewmodel.LoginViewModelFactory
import com.example.fundooapp.viewmodel.SharedViewModel
import com.example.fundooapp.viewmodel.SharedViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class HomeFragment : Fragment(R.layout.fragment_home) {
    lateinit var fabButton: FloatingActionButton
    private lateinit var sharedViewModel : SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_home, container, false)
        sharedViewModel = ViewModelProvider(requireActivity(), SharedViewModelFactory(UserAuthService()))[SharedViewModel::class.java]
        /*customToolbar = view.findViewById(R.id.customToolbar)
        drawer = view.findViewById(R.id.drawerLayout)
        navView = view.findViewById(R.id.navigationView)*/
        /*(activity as AppCompatActivity?)!!.setSupportActionBar(customToolbar)
        (activity as AppCompatActivity?)!!.supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
        (activity as AppCompatActivity?)!!.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity?)!!.supportActionBar?.setDisplayUseLogoEnabled(true)
        toggle = ActionBarDrawerToggle(requireActivity(), drawer, R.string.open, R.string.close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)*/
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fabButton = view.findViewById(R.id.FAV_addNote)
        createNewNotes()
    }

 /*   override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.custom_toolbar, menu)
    }
*/
 /*   override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item))
            return true
        var item_id = item.itemId
        if (item_id == R.id.share)
            Toast.makeText(requireContext(), "Share selected", Toast.LENGTH_SHORT).show()
        if (item_id == R.id.exit)
            Toast.makeText(requireContext(), "Exit App", Toast.LENGTH_SHORT).show()
        if (item_id == R.id.about)
            Toast.makeText(requireContext(), "About", Toast.LENGTH_SHORT).show()
        if (item_id == R.id.settings)
            Toast.makeText(requireContext(), "Settings", Toast.LENGTH_SHORT).show()
        if (item_id == R.id.search)
            Toast.makeText(requireContext(), "Search", Toast.LENGTH_SHORT).show()
        return true
    }*/

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
}