package com.example.auracle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.example.auracle.firebase.Authenticate

class SignUp : AppCompatActivity() {

    private val TAG = "SignUp"

    private lateinit var txtNewEmail: EditText
    private lateinit var txtPassword: EditText
    private lateinit var btnSignUp: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        txtNewEmail = findViewById(R.id.newEmail)
        txtPassword = findViewById(R.id.enterPassword)
        btnSignUp = findViewById(R.id.Signupkorobutton)


        btnSignUp.setOnClickListener{
            signUp()
        }
    }

    private fun signUp(): Boolean {
        val email = txtNewEmail.text
        val password = txtPassword.text

        if (email.isBlank() || email.isEmpty()) return false
        if (password.isBlank() || password.isEmpty()) return false

        val authService = Authenticate()
        Log.d(TAG, authService.user().toString())

        authService.signUp(email.toString(), password.toString())
            .addOnCompleteListener() {  task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, Homepage::class.java))

                }
            }

//        Log.d(TAG, authService.user().toString())

        return true
    }

}