package com.dicoding.picodiploma.loginwithanimation.view.dashboard

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Setup the drawer layout and bottom navigation
        setupNavigation()

        // Optionally, handle any intent extras
        val userModel: UserModel? = intent.getParcelableExtra("USER_MODEL")
        userModel?.let {
            // Use the user data (e.g., show username in the header)
            findViewById<TextView>(R.id.user_name).text = it.name
            findViewById<TextView>(R.id.user_email).text = it.email
        }
    }

    private fun setupNavigation() {
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        val sideNavView = findViewById<NavigationView>(R.id.side_nav_view)

        // BottomNavigationView item selection listener
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Handle navigation to home
                    true
                }
                R.id.nav_story -> {
                    // Handle navigation to story
                    true
                }
                R.id.nav_my_story -> {
                    // Handle navigation to my story
                    true
                }
                else -> false
            }
        }

        // Handle side navigation menu item selection
        sideNavView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard -> {
                    // Handle navigation to dashboard
                    true
                }
                R.id.nav_maps -> {
                    // Handle navigation to maps
                    true
                }
                R.id.nav_logout -> {
                    // Handle logout logic
                    finish() // Optionally finish activity to log out
                    true
                }
                else -> false
            }
        }
    }
}
