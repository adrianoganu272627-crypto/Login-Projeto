package com.example.loginprojeto

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import com.example.loginprojeto.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    companion object {
        const val EXTRA_USERNAME = "extra_username"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdgeInsets()
        setupTextWatchers()
        setupListeners()
    }

    private fun setupEdgeToEdgeInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.loginContainer) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupListeners() {
        binding.loginButton.setOnClickListener {
            validateAndLogin()
        }

        binding.forgotPasswordTextView.setOnClickListener {
            Toast.makeText(this, getString(R.string.forgot_password), Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupTextWatchers() {
        binding.userEditText.doOnTextChanged { _, _, _, _ ->
            binding.userInputLayout.error = null
        }

        binding.passwordEditText.doOnTextChanged { _, _, _, _ ->
            binding.passwordInputLayout.error = null
        }
    }

    private fun validateAndLogin() {
        val userText = binding.userEditText.text?.toString()?.trim().orEmpty()
        val passwordText = binding.passwordEditText.text?.toString()?.trim().orEmpty()

        var isValid = true

        if (userText.isEmpty()) {
            binding.userInputLayout.error = getString(R.string.error_empty_user)
            isValid = false
        } else {
            binding.userInputLayout.error = null
        }

        if (passwordText.isEmpty()) {
            binding.passwordInputLayout.error = getString(R.string.error_empty_password)
            isValid = false
        } else if (passwordText.length < 6) {
            binding.passwordInputLayout.error = getString(R.string.error_short_password)
            isValid = false
        } else {
            binding.passwordInputLayout.error = null
        }

        if (isValid) {
            performLogin(userText)
        }
    }

    private fun performLogin(username: String) {
        showLoading(isLoading = true)

        // Simula login e navega para a MainActivity
        binding.root.postDelayed({
            showLoading(isLoading = false)
            navigateToMainActivity(username)
        }, 1000)
    }

    private fun navigateToMainActivity(username: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra(EXTRA_USERNAME, username)
        }
        startActivity(intent)
        finish()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.loginButton.isEnabled = !isLoading
    }
}