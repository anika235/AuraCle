package com.example.auracle.com.example.auracle.api.firebase

import com.example.auracle.com.example.auracle.datapack.User
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class FirebaseRealtime private constructor() {

    companion object {
        private var instance: FirebaseRealtime? = null

        fun getInstance(): FirebaseRealtime {
            if (instance == null)
                instance = FirebaseRealtime()
            return instance!!
        }
    }

    private val database = FirebaseDatabase.getInstance("https://auracle-85d79-default-rtdb.asia-southeast1.firebasedatabase.app/").reference

    fun createUser(user: User): Task<Void> {
        return database.child("users").child(user.uid).setValue(user)
    }

    fun getCurrentUserDetails(): Task<DataSnapshot>? {
        val currentUser = Authenticate.getInstance().user() ?: return null
        return database.child("users").child(currentUser.uid).get()
    }

}