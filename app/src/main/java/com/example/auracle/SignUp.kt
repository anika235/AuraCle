package com.example.auracle

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.auracle.databinding.ActivitySignUpBinding
import com.example.auracle.firebase.Authenticate
import com.google.android.material.snackbar.Snackbar

class SignUp : AppCompatActivity() {

    private val TAG = "SignUp"

    private lateinit var binding: ActivitySignUpBinding
    private val auth = Authenticate()


    override fun onCreate(savedInstanceState: Bundle?) {

//        If Logged in, switches to homepage
        if (auth.isSignedIn())
            switchToHome()

        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignUp.setOnClickListener {
            signUp()
        }

        binding.btnSignUpToSignIn.setOnClickListener {
            startActivity(Intent(this, LoginPage::class.java))
            finish()
        }

    }

    private fun signUp() {

        if (!validForm()) {
            Log.d(TAG, "Invalid Form")
            return
        }

        val email = binding.txtSignUpEmail.text
        val password = binding.txtSignUpPassword.text

        val authService = Authenticate()
        Log.d(TAG, authService.user().toString())

        authService.signUp(email.toString(), password.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful)
                    switchToHome()
                else
                    Snackbar.make(
                        binding.btnSignUp,
                        "Account not Created: ${task.exception?.message}",
                        Snackbar.LENGTH_LONG
                    ).show()

            }

//        Log.d(TAG, authService.user().toString())
    }

    private fun validForm(): Boolean {

        var validForm = true

        val txtNewEmail = binding.txtSignUpEmail
        val txtPassword = binding.txtSignUpPassword

        if (txtNewEmail.text.isBlank() || txtNewEmail.text.isEmpty()) {
            txtNewEmail.error = "Email is empty"
            txtNewEmail.requestFocus()
            validForm = false
        } else {
            txtNewEmail.error = null
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