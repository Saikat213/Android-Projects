package com.example.fundooapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fundooapp.model.NotesData
import com.example.fundooapp.model.UserAuthService
import com.example.fundooapp.viewmodel.SharedViewModel
import com.example.fundooapp.viewmodel.SharedViewModelFactory
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var sharedViewModel: SharedViewModel
    lateinit var drawer : DrawerLayout
    lateinit var navView : NavigationView
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var customToolbar: androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        customToolbar = findViewById(R.id.customToolbar)
        drawer = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.navigationView)
        sharedViewModel = ViewModelProvider(
            this,
            SharedViewModelFactory(UserAuthService())
        )[SharedViewModel::class.java]

        setSupportActionBar(customToolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayUseLogoEnabled(true)
        toggle = ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close)
        drawer.addDrawerListener(toggle)
        toggle.isDrawerIndicatorEnabled = true

        toggle.syncState()
        sharedViewModel.gotoLoginPage(true)
        observeAppNav()
    }

    private fun observeAppNav() {
        sharedViewModel.gotoLoginPageStatus.observe(this, Observer {
            if (it == true) {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragmentContainer, LoginFragment())
                    commit()
                }
                supportActionBar?.hide()
            }
        })
        sharedViewModel.gotoRegistrationPageStatus.observe(this, Observer {
            if (it == true) {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragmentContainer, RegisterFragment())
                    commit()
                }
                supportActionBar?.hide()
            }
        })
        sharedViewModel.gotoHomePageStatus.observe(this, Observer {
            if (it == true) {
                supportActionBar?.show()
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragmentContainer, HomeFragment())
                    commit()
                }
            }
        })
        sharedViewModel.gotoCreateNotesPageStatus.observe(this, Observer {
            if (it == true) {
                supportActionBar?.hide()
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragmentContainer, CreateNotesFragment())
                    addToBackStack(null)
                    commit()
                }

            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item))
            return true
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.custom_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun toolbarIcon(context: Context, toolbar: Toolbar) {
        toolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.profile -> {
                    Toast.makeText(context, "Profile", Toast.LENGTH_SHORT).show()
                }
                R.id.signout -> {
                    var firebaseAuth = FirebaseAuth.getInstance()
                    firebaseAuth.signOut()
                    finish()
                    startActivity(Intent(context, MainActivity::class.java))
                    Toast.makeText(this, "Check", Toast.LENGTH_SHORT).show()
                    /*supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragmentContainer, LoginFragment())
                        commit()
                    }*/
                }
            }
           true
        }
    }
/*
    fun onNavigationItemSelected(drawerL : DrawerLayout, nav : NavigationView, context: Context) {
        nav.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.notes2345 -> {
                    Toast.makeText(context, "Notes", Toast.LENGTH_SHORT).show()
                    drawerL.closeDrawers()
                }
                R.id.archive -> {
                    Toast.makeText(context, "Archive", Toast.LENGTH_SHORT).show()
                    drawerL.closeDrawers()
                }
            }
            true
        }
    }*/
}