package com.dicoding.picodiploma.loginwithanimation.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivitySignupBinding
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import kotlinx.coroutines.launch
import com.dicoding.picodiploma.loginwithanimation.data.api.Result  // Import the correct Result class from UserRepository


class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()
    }


    private fun playAnimation() {
        // Animasi untuk imageView
        val imageTranslation = ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }

        // Animasi fade-in untuk elemen lainnya
        val fadeInImage = ObjectAnimator.ofFloat(binding.imageView, View.ALPHA, 0f, 1f).setDuration(700)
        val fadeInTitle = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 0f, 1f).setDuration(700)
        val fadeInNameText = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 0f, 1f).setDuration(700)
        val fadeInNameInput = ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 0f, 1f).setDuration(700)
        val fadeInEmailText = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 0f, 1f).setDuration(700)
        val fadeInEmailInput = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 0f, 1f).setDuration(700)
        val fadeInPasswordText = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 0f, 1f).setDuration(700)
        val fadeInPasswordInput = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 0f, 1f).setDuration(700)
        val fadeInSignupButton = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 0f, 1f).setDuration(700)

        // AnimatorSet untuk menjalankan animasi secara bertahap
        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(
            fadeInImage,
            fadeInTitle,
            fadeInNameText,
            fadeInNameInput,
            fadeInEmailText,
            fadeInEmailInput,
            fadeInPasswordText,
            fadeInPasswordInput,
            fadeInSignupButton
        )
        animatorSet.startDelay = 100 // Tambahkan delay sebelum animasi dimulai
        animatorSet.start()

        // Memulai animasi translasi secara independen
        imageTranslation.start()
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
        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                showErrorDialog("Semua field harus diisi!")
            } else if (password.length < 8) {
                showErrorDialog("Password harus minimal 8 karakter!")
            } else {
                lifecycleScope.launch {
                    UserRepository.register(name, email, password).collect { result ->
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

                            is com.dicoding.picodiploma.loginwithanimation.data.api.Result.Error -> {
                                TODO()
                            }
                            com.dicoding.picodiploma.loginwithanimation.data.api.Result.Loading -> {
                                TODO()
                            }
                            is com.dicoding.picodiploma.loginwithanimation.data.api.Result.Success -> {
                                TODO()
                            }
                        }
                    }
                }
            }
        }
    }
    private fun showAlertDialog(message: String) {
        AlertDialog.Builder(this).apply {
            setTitle("Info")
            setMessage(message)
            setPositiveButton("OK") { _, _ -> }
            create()
            show()
        }
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

    private fun showSuccessDialog(message: String) {
        AlertDialog.Builder(this).apply {
            setTitle("Sukses!")
            setMessage(message)
            setPositiveButton("OK") { _, _ -> finish() }
            create()
            show()
        }
    }


}