package com.example.fundooapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fundooapp.model.UserAuthService
import com.example.fundooapp.viewmodel.SharedViewModel
import com.example.fundooapp.viewmodel.SharedViewModelFactory
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

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
        toggle.syncState()
        drawer.addDrawerListener(toggle)
        navView.setNavigationItemSelectedListener(this)
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
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragmentContainer, CreateNotesFragment())
                    addToBackStack(null)
                    commit()
                }
                supportActionBar?.hide()
            }
        })
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        var itemID = menuItem.itemId
        when(itemID) {
            R.id.notes -> Toast.makeText(this, "Notes", Toast.LENGTH_SHORT).show()
            R.id.archive -> Toast.makeText(this, "Archive", Toast.LENGTH_SHORT).show()
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
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
}