package com.dicoding.picodiploma.loginwithanimation.view.login

import DashboardFragment
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.dicoding.picodiploma.loginwithanimation.DashboardActivity
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityLoginBinding
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import kotlinx.coroutines.launch
import com.dicoding.picodiploma.loginwithanimation.data.api.Result
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                showErrorDialog("Email dan Password harus diisi!")
            } else {
                lifecycleScope.launch {
                    UserRepository.login(email, password).collect { result ->
                        when (result) {
                            is Result.Loading -> {
                                showLoading(true)
                            }
                            is Result.Success -> {
                                showLoading(false)

                                // Ambil data login dari API
                                val loginResult = result.data
                                val userModel = UserModel(
                                    email = loginResult.email,
                                    token = loginResult.token,
                                    name = loginResult.name,   // Ambil nama dari API
                                    isLogin = true
                                )

                                saveUserToPreferences(userModel)  // Simpan data ke SharedPreferences (opsional)
                                navigateToDashboard(userModel)
                            //showSuccessDialog(userModel)      // Tampilkan dialog sukses
                            }
                            is Result.Error -> {
                                showLoading(false)
                                showErrorDialog(result.error)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun saveUserToPreferences(userModel: UserModel) {
        val sharedPref = getSharedPreferences("USER_PREF", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("email", userModel.email)
            putString("token", userModel.token)
            putString("name", userModel.name)  // Simpan nama
            putBoolean("isLogin", userModel.isLogin)
            apply()
        }
    }

    private fun navigateToDashboard(userModel: UserModel) {
        val intent = Intent(this, DashboardActivity::class.java).apply {
            putExtra("USER_MODEL", userModel)
        }
        startActivity(intent)
        finish() // Close LoginActivity after navigating to DashboardActivity
    }

    private fun playAnimation() {

        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_Y, -20f, 20f).apply {
            duration = 3000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        // Animasi untuk elemen-elemen dalam halaman login
        val fadeInEmailText = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 0f, 1f).setDuration(400)
        val fadeInEmailInput = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 0f, 1f).setDuration(400)
        val fadeInPasswordText = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 0f, 1f).setDuration(400)
        val fadeInPasswordInput = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 0f, 1f).setDuration(400)
        val fadeInLoginButton = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 0f, 1f).setDuration(400)
        val fadeInImage = ObjectAnimator.ofFloat(binding.imageView, View.ALPHA, 0f, 1f).setDuration(400)
        val messageTextView = ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 0f, 1f).setDuration(400)
        val titleTextView = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 0f, 1f).setDuration(400)

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(
            fadeInImage,
            titleTextView,
            messageTextView,
            fadeInEmailText,
            fadeInEmailInput,
            fadeInPasswordText,
            fadeInPasswordInput,
            fadeInLoginButton
        )
        animatorSet.startDelay = 50
        animatorSet.start()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(this).apply {
            setTitle("Error")
            setMessage(message)
            setPositiveButton("OK", null)
            create()
            show()
        }
    }

    private fun showSuccessDialog(userModel: UserModel) {
        AlertDialog.Builder(this).apply {
            setTitle("Login Sukses!")
            setMessage("Selamat datang, ${userModel.name}")  // Tampilkan nama pengguna
            setPositiveButton("OK") { _, _ ->
                // Navigate to DashboardFragment
                val fragment = DashboardFragment()

// Pass the userModel to the fragment using arguments
                val bundle = Bundle()
                bundle.putParcelable("USER_MODEL", userModel)
                fragment.arguments = bundle

// Replace the current fragment with DashboardFragment
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, DashboardFragment()) // Ganti dengan ID yang sesuai dengan kontainer Fragment Anda
                    .addToBackStack(null) // Optional: jika Anda ingin menambahkan ke back stack
                    .commit()

                finish() // Selesaikan LoginActivity setelah mengganti fragment
                // Finish the LoginActivity after fragment transaction
            }
            create()
            show()
        }
    }



    /*private fun showSuccessDialog(message: String) {
        AlertDialog.Builder(this).apply {
            setTitle("Login Sukses!")
            setMessage(message)
            setPositiveButton("OK") { _, _ ->
                // Arahkan ke halaman utama setelah login sukses
                finish()
            }
            create()
            show()
        }
    }*/
}
