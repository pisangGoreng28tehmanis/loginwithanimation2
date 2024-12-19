package com.dicoding.picodiploma.loginwithanimation.view.dashboard


import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityDashboardBinding
import com.dicoding.picodiploma.loginwithanimation.view.fragment.story.StoryFragment


class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var userModel: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Enable edge-to-edge UI
        enableEdgeToEdge()

        val userModel = intent.getParcelableExtra<UserModel>("USER_MODEL")
        userModel?.let {
            // Use userModel data here
            // For example, display user information
            println("User Name: ${it.name}")
        }
        val storyFragment = StoryFragment().apply {
            arguments = Bundle().apply {
                putParcelable("USER_MODEL", userModel)
            }
        }
        /*supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, storyFragment)
            .commit()*/

        // Retrieve user model from the Intent
        //userModel = intent.getParcelableExtra("USER_MODEL")!!

        // Display a welcome message
        //binding.welcomeTextView.text = "Selamat datang, ${userModel.name}"

        // Set up Bottom Navigation with Navigation Component
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController: NavController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)

        // Handle window insets for edge-to-edge UI
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}

