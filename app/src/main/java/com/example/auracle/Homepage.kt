package com.example.auracle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.auracle.firebase.Authenticate

class Homepage : AppCompatActivity() {

    private val TAG = "HomePage"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        Log.d(TAG, Authenticate().user().toString())

    }
}