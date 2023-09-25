package com.example.auracle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.example.auracle.firebase.Authenticate
import com.google.android.material.snackbar.Snackbar
import java.util.Timer
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.concurrent.schedule

class LoginPage : AppCompatActivity() {

    private val TAG = "Login Page"
    private lateinit var txtEmail: EditText
    private lateinit var txtPassword: EditText
    private lateinit var btnSignIn: Button
    private val auth = Authenticate()

    override fun onCreate(savedInstanceState: Bundle?) {

//        If Logged in, switches to homepage
        if (auth.isSignedIn())
            switchToHome()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        txtEmail = findViewById(R.id.email)
        txtPassword = findViewById(R.id.confirm_password)
        btnSignIn = findViewById(R.id.loginbutton)

        btnSignIn.setOnClickListener{
            signIn()
        }
    }

    private fun signIn() {

        if (!validForm())
            return

        val email = txtEmail.text
        val password = txtPassword.text

        auth.signIn(email.toString(), password.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful)
                    switchToHome()

                else
                    Snackbar.make(btnSignIn, "Login Unsuccessful: ${task.exception?.message}", Snackbar.LENGTH_LONG).show()

            }
    }

    private fun validForm(): Boolean {

        var validForm = true

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