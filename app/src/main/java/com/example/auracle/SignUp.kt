package com.example.auracle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.auracle.firebase.Authenticate
import com.google.android.material.snackbar.Snackbar
import java.util.Timer
import kotlin.concurrent.schedule

class SignUp : AppCompatActivity() {

    private val TAG = "SignUp"

    private lateinit var txtNewEmail: EditText
    private lateinit var txtPassword: EditText
    private lateinit var btnSignUp: Button
    private lateinit var btnSwitchToSignIn: TextView
    private val auth = Authenticate()


    override fun onCreate(savedInstanceState: Bundle?) {

//        If Logged in, switches to homepage
        if (auth.isSignedIn())
            switchToHome()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        txtNewEmail = findViewById(R.id.newEmail)
        txtPassword = findViewById(R.id.enterPassword)
        btnSignUp = findViewById(R.id.Signupkorobutton)
        btnSwitchToSignIn = findViewById(R.id.switchToSignIn)


        btnSignUp.setOnClickListener{
            signUp()
            Log.d(TAG, "After Signup")
        }
        btnSwitchToSignIn.setOnClickListener {
            startActivity(Intent(this, LoginPage::class.java))
            finish()
        }
    }

    private fun signUp() {

        if (!validForm()) {
            Log.d(TAG, "Invalid Form")
            return
        }

        val email = txtNewEmail.text
        val password = txtPassword.text

        val authService = Authenticate()
        Log.d(TAG, authService.user().toString())

        authService.signUp(email.toString(), password.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful)
                    switchToHome()

                else
                    Snackbar.make(btnSignUp, "Account not Created: ${task.exception?.message}", Snackbar.LENGTH_LONG).show()

            }

//        Log.d(TAG, authService.user().toString())
    }

    private fun validForm(): Boolean {

        var validForm = true

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