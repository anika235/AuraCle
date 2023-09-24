package com.example.auracle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class LoginPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        var buttonlogin = findViewById<Button>(R.id.loginbutton)
        buttonlogin.setOnClickListener{
            val intent = Intent(this, Homepage::class.java)
            startActivity(intent)
        }
    }
}