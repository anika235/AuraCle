package com.example.auracle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.auracle.firebase.Authenticate

class StartPage : AppCompatActivity() {

    private val TAG = "StartPage"

    override fun onCreate(savedInstanceState: Bundle?) {

//        Is Logged in, switches to homepage
        if (Authenticate().isSignedIn())
            startActivity(Intent(this, Homepage::class.java))

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startpage)

        var buttonlogin = findViewById<Button>(R.id.signinbutton)
        buttonlogin.setOnClickListener{
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
        }

        var buttonSignup = findViewById<Button>(R.id.signupbutton)
        buttonSignup.setOnClickListener{
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }
        Log.d(TAG, Authenticate().user()?.email.toString())
    }
}