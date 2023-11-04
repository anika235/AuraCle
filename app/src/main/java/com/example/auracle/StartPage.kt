package com.example.auracle

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.auracle.databinding.ActivityStartpageBinding
import com.example.auracle.firebase.Authenticate

class StartPage : AppCompatActivity() {

    private val TAG = "StartPage"
    private lateinit var binding: ActivityStartpageBinding

    override fun onCreate(savedInstanceState: Bundle?) {


//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

//        Is Logged in, switches to homepage
        if (Authenticate().isSignedIn())
//            startActivity(Intent(this, Homepage::class.java))
            startActivity(Intent(this, PodcastDetails::class.java).apply {
                putExtra("podcast_id", "ea09b575d07341599d8d5b71f205517b")
            })

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
//        Log.d(TAG, Authenticate().user()?.email.toString())
    }
}