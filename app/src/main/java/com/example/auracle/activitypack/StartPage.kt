package com.example.auracle.com.example.auracle.activitypack

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.example.auracle.databinding.ActivityStartpageBinding

class StartPage : Activity() {

    private val TAG = "StartPage"
    private lateinit var binding: ActivityStartpageBinding

    override fun onCreate(savedInstanceState: Bundle?) {


//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        super.onCreate(savedInstanceState)

        binding = ActivityStartpageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignIn.setOnClickListener{
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
        }

        binding.btnSignUp.setOnClickListener{
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }
    }
}