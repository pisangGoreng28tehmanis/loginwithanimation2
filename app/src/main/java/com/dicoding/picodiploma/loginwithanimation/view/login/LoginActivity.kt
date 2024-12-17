package com.dicoding.picodiploma.loginwithanimation.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
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
                                showSuccessDialog(result.data)
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

    private fun playAnimation() {
        // Animasi untuk elemen-elemen dalam halaman login
        val fadeInEmailText = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 0f, 1f).setDuration(700)
        val fadeInEmailInput = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 0f, 1f).setDuration(700)
        val fadeInPasswordText = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 0f, 1f).setDuration(700)
        val fadeInPasswordInput = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 0f, 1f).setDuration(700)
        val fadeInLoginButton = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 0f, 1f).setDuration(700)

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(
            fadeInEmailText,
            fadeInEmailInput,
            fadeInPasswordText,
            fadeInPasswordInput,
            fadeInLoginButton
        )
        animatorSet.startDelay = 100
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
            setMessage("Selamat datang, ${userModel.email}") // Or any other property of UserModel
            setPositiveButton("OK") { _, _ ->
                // Navigate to the main page after successful login
                finish()
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
