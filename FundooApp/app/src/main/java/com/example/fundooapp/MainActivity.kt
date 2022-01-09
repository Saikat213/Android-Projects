package com.example.fundooapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuItemCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fundooapp.model.NotesData
import com.example.fundooapp.model.NotesServiceImpl
import com.example.fundooapp.model.UserAuthService
import com.example.fundooapp.network.ApiClient
import com.example.fundooapp.network.ApiInterface
import com.example.fundooapp.viewmodel.NoteAdapter
import com.example.fundooapp.viewmodel.SharedViewModel
import com.example.fundooapp.viewmodel.SharedViewModelFactory
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        toggle = ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.isDrawerIndicatorEnabled = true
        sharedViewModel.gotoLoginPage(true)
        //retrofitServices(this)
        observeAppNav()
        onNavigationItemSelected(drawer, navView, this)
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
                    addToBackStack(null)
                    commit()
                }
                supportActionBar?.hide()
            }
        })
        sharedViewModel.gotoHomePageStatus.observe(this, Observer {
            if (it == true) {
                supportActionBar?.show()
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragmentContainer, HomeFragment(), "homeFragment")
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

    fun onNavigationItemSelected(drawerL : DrawerLayout, nav : NavigationView, context: Context) {
        nav.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.notes2345 -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragmentContainer, HomeFragment())
                        commit()
                    }
                    drawerL.closeDrawers()
                }
                R.id.archive -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragmentContainer, ArchiveFragment())
                        addToBackStack(null)
                        commit()
                    }
                    drawerL.closeDrawers()
                }
                R.id.nav_share -> Toast.makeText(context, "Share Clicked", Toast.LENGTH_SHORT).show()
                R.id.nav_send -> Toast.makeText(context, "Send Clicked", Toast.LENGTH_SHORT).show()
            }
            true
        }
    }

    fun retrofitServices(context: Context) {
        val retrofit = ApiClient.getApiClient()
        val api = retrofit.create(ApiInterface::class.java)
        api.fetchAllData().enqueue(object : Callback<List<NotesData>> {
            override fun onResponse(
                call: Call<List<NotesData>>,
                response: Response<List<NotesData>>
            ) {
                Log.d("Retrofit-->", "${response.body()}")
                sharedViewModel.gotoHomePage(true)
            }

            override fun onFailure(call: Call<List<NotesData>>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}