package com.example.auracle

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.auracle.com.example.auracle.api.firebase.Authenticate

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler(Looper.getMainLooper()).postDelayed({
            if (Authenticate().isSignedIn())
                startActivity(Intent(this, Homepage::class.java))

            else
                startActivity(Intent(this, StartPage::class.java))
            finish()
        }, 2000)

    }

}