package com.example.auracle

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.auracle.com.example.auracle.api.firebase.Authenticate
import com.example.auracle.com.example.auracle.api.firebase.FirebaseRealtime
import com.example.auracle.com.example.auracle.datapack.User
import com.example.auracle.databinding.ActivitySignUpBinding
import com.google.android.material.snackbar.Snackbar

class SignUp : AppCompatActivity() {

    private val TAG = "SignUp"

    private lateinit var binding: ActivitySignUpBinding
    private val auth = Authenticate.getInstance()
    private val firebaseRealtime = FirebaseRealtime.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {

//        If Logged in, switches to homepage
        if (auth.isSignedIn())
            switchToHome()

        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignUp.setOnClickListener {
            signUp()
        }

        binding.btnSignUpToSignIn.setOnClickListener {
            switchToSignIn()
        }
        val imageViewpass: ImageView = binding.eye
        imageViewpass.setImageResource(R.drawable.hide_pass)
        imageViewpass.setOnClickListener {
            if (binding.txtSignUpPassword.transformationMethod == HideReturnsTransformationMethod.getInstance()) {
                binding.txtSignUpPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                imageViewpass.setImageResource(R.drawable.hide_pass)
            } else {
                binding.txtSignUpPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                imageViewpass.setImageResource(R.drawable.show_pass)
            }
        }


    }

    private fun signUp() {

        if (!validForm()) {
            Log.d(TAG, "Invalid Form")
            return
        }

        val name = binding.txtSignUpName.text
        val email = binding.txtSignUpEmail.text
        val password = binding.txtSignUpPassword.text

        val user = User(email.toString(), name.toString(), password.toString(), "")


        auth.signUp(user)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    auth.signIn(user).addOnCompleteListener {
                        user.uid = auth.user()?.uid.toString()

                        Log.w(TAG, "User UID: ${user.uid}")

                        firebaseRealtime.createUser(user).addOnCompleteListener{

                            if (task.isSuccessful)
                                Snackbar.make(binding.btnSignUp, "Account created Successfully", Snackbar.LENGTH_LONG).show()
                            else
                                Snackbar.make(binding.btnSignUp, "Account Creation Interrupted: ${task.exception?.message}", Snackbar.LENGTH_LONG).show()

                            switchToHome()
                        }

                    }
                } else
                    Snackbar.make(binding.btnSignUp, "Account not Created: ${task.exception?.message}", Snackbar.LENGTH_LONG).show()

            }

//        Log.d(TAG, authService.user().toString())
    }

    private fun validForm(): Boolean {

        var validForm = true

        val txtName = binding.txtSignUpName
        val txtNewEmail = binding.txtSignUpEmail
        val txtPassword = binding.txtSignUpPassword

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

        if (txtName.text.isBlank() || txtName.text.isEmpty()) {
            txtName.error = "Password is empty"
            if (validForm) txtName.requestFocus()
            validForm = false
        } else {
            txtName.error = null
        }

        return validForm
    }

    private fun switchToSignIn() {
        startActivity(Intent(this, LoginPage::class.java))
        finish()
    }

    private fun switchToHome() {
        startActivity(Intent(this, Homepage::class.java))
        finish()
    }

}