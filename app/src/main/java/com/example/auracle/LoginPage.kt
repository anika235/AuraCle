package com.example.auracle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.auracle.firebase.Authenticate

class LoginPage : AppCompatActivity() {

    private lateinit var txtEmail: EditText
    private lateinit var txtPassword: EditText
    private lateinit var btnSignIn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
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
        val email = txtEmail.text
        val password = txtPassword.text

        if (email.isBlank() || email.isEmpty()) return
        if (password.isBlank() || password.isEmpty()) return

        Authenticate().signIn(email.toString(), password.toString())
            .addOnCompleteListener() {  task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, Homepage::class.java))
                }
            }
    }

}