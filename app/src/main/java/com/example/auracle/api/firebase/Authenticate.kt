package com.example.auracle.com.example.auracle.api.firebase

import android.content.Context
import android.util.Log
import com.example.auracle.com.example.auracle.datapack.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Authenticate private constructor() {
//    private val TAG: String = "Authenticate"
    private val auth: FirebaseAuth = Firebase.auth

    companion object {
        private var instance: Authenticate? = null

        fun getInstance(): Authenticate {
            if (instance == null)
                instance = Authenticate()
            return instance!!
        }
    }

    fun signUp(user: User): Task<AuthResult> {
        return auth.createUserWithEmailAndPassword(user.email, user.password)
    }

    fun signIn(user: User): Task<AuthResult> {
        return auth.signInWithEmailAndPassword(user.email, user.password)
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