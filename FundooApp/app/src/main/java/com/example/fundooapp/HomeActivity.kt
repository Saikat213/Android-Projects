package com.example.fundooapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var toggle : ActionBarDrawerToggle
    lateinit var drawarLayout : DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //var frameLayout = findViewById<FrameLayout>(R.id.frame)
        var list = findViewById<NavigationView>(R.id.navigationView)
        drawarLayout = findViewById(R.id.drawerLayout)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle = ActionBarDrawerToggle(this, drawarLayout, R.string.open, R.string.close)
        toggle.syncState()

        list.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawarLayout.isDrawerOpen(GravityCompat.START))
            drawarLayout.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item))
            return true
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.notes ->
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragmentContainerView, NotesFragment())
                    commit()
                }

            R.id.archive ->
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragmentContainerView, ArchiveFragment())
                    commit()
                }

            R.id.nav_share ->
                Toast.makeText(this, "Share", Toast.LENGTH_LONG).show()

            R.id.nav_send ->
                Toast.makeText(this, "Send", Toast.LENGTH_LONG).show()
        }
        drawarLayout.closeDrawer(GravityCompat.START)
        return true
    }
}