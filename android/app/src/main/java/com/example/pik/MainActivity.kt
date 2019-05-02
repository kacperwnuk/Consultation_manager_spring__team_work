package com.example.pik

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.pik.REST.Repository
import com.example.pik.data.CredentialsManager
import com.google.android.gms.auth.api.credentials.Credential
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    ReserveConsultationFragment.ActionListener,
    ViewReservedConsultationsFragment.ActionListener {

    var credential: Credential? = null
    private lateinit var repository: Repository

    private lateinit var reserveConsultationFragment: ReserveConsultationFragment
    private lateinit var viewReservedConsultationsFragment: ViewReservedConsultationsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Reserve consultation"

        reserveConsultationFragment = ReserveConsultationFragment.newInstance("", "")
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, reserveConsultationFragment).commit()

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
        if (savedInstanceState != null) {
            return
        }
        credential = intent.extras.get("Credential") as Credential?
        repository = Repository(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.find_consultation -> {
                reserveConsultationFragment = ReserveConsultationFragment.newInstance("", "")
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, reserveConsultationFragment)
                    .addToBackStack(null).commit()
                supportActionBar!!.title = "Reserve consultation"
            }
            R.id.my_consultations -> {
                viewReservedConsultationsFragment = ViewReservedConsultationsFragment.newInstance()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, viewReservedConsultationsFragment)
                    .addToBackStack(null).commit()
                supportActionBar!!.title = "My consultations"
            }
            R.id.settings -> {

            }
            R.id.log_out -> {
                CredentialsManager(this).deleteCredentials(credential)
                val loginIntent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(loginIntent)
                finish()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun reserve(id: String) {
        try {
            repository.reserveConsultation(id, credential!!.id)
            reserveConsultationFragment.update()
        } catch (e: Exception) {
            Toast.makeText(this, "Network error occurred", Toast.LENGTH_LONG).show()
        }
    }

    override fun cancelConsultation(id: String) {
        try {
            repository.cancelConsultation(id, credential!!.id)
            viewReservedConsultationsFragment.update()
        } catch (e: Exception) {
            Toast.makeText(this, "Network error occurred", Toast.LENGTH_LONG).show()
        }
    }
}
