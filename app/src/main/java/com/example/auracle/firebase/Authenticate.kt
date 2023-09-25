package com.example.auracle.firebase

import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Authenticate {
    private val TAG: String = "Authenticate"
    private val auth: FirebaseAuth = Firebase.auth

    fun signUp(email: String, password: String): Task<AuthResult> {
        return auth.createUserWithEmailAndPassword(email, password)
    }

    fun signIn(email: String, password: String): Task<AuthResult> {
        return auth.signInWithEmailAndPassword(email, password)
    }

    fun signOut() {
        auth.signOut()
    }

    fun user(): FirebaseUser? {
        return auth.currentUser
    }

    fun isSignedIn(): Boolean {
        this.user() ?: return false // fancy kotlin way to check if user is null
        return true
    }

}