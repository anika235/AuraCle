package com.example.auracle.com.example.auracle.activitypack

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.auracle.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var continueButton: Button
    private lateinit var forgotMail: EditText
    private lateinit var auth: FirebaseAuth

    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        continueButton = binding.continuebutton
        forgotMail = binding.forgotmail

        continueButton.setOnClickListener {
            validateData()
        }
    }

    private fun validateData() {
        val email = forgotMail.text.toString()
        if (email.isEmpty()) {
            forgotMail.error = "Email is empty"
            forgotMail.requestFocus()
        } else {
            forpass(email)
        }
    }

    private fun forpass(email:String) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Check Your Mail", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginPage::class.java))
                finish()
            } else {
                Toast.makeText(
                    this,
                    "Error : " + task.exception?.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
