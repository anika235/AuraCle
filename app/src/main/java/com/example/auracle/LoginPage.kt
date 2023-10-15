package com.example.auracle

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.auracle.databinding.ActivityLoginPageBinding
import com.example.auracle.firebase.Authenticate
import com.google.android.material.snackbar.Snackbar

class LoginPage : AppCompatActivity() {

    private val TAG = "Login Page"

    private lateinit var binding: ActivityLoginPageBinding

    private val auth = Authenticate()

    override fun onCreate(savedInstanceState: Bundle?) {

//        If Logged in, switches to homepage
        if (auth.isSignedIn())
            switchToHome()

        super.onCreate(savedInstanceState)

        binding = ActivityLoginPageBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.btnSignIn.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {

        if (!validForm())
            return

        val email = binding.txtEmail.text
        val password = binding.txtPassword.text

        auth.signIn(email.toString(), password.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful)
                    switchToHome()
                else
                    Snackbar.make(
                        binding.btnSignIn,
                        "Login Unsuccessful: ${task.exception?.message}",
                        Snackbar.LENGTH_LONG
                    ).show()

            }
    }

    private fun validForm(): Boolean {

        var validForm = true

        val txtEmail = binding.txtEmail
        val txtPassword = binding.txtPassword

        if (txtEmail.text.isBlank() || txtEmail.text.isEmpty()) {
            txtEmail.error = "Email is empty"
            txtEmail.requestFocus()
            validForm = false
        } else {
            txtEmail.error = null
        }

        if (txtPassword.text.isBlank() || txtPassword.text.isEmpty()) {
            txtPassword.error = "Password is empty"
            if (validForm) txtPassword.requestFocus()
            validForm = false
        } else {
            txtPassword.error = null
        }
        return validForm
    }

    private fun switchToHome() {
        startActivity(Intent(this, Homepage::class.java))
        finish()
    }

}