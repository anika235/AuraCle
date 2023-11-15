package com.example.auracle.com.example.auracle.api.firebase

import com.example.auracle.com.example.auracle.datapack.User
import com.example.auracle.datapack.listennote.ListenEpisodeShort
import com.example.auracle.datapack.listennote.ListenSearchPodcast
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class FirebaseRealtime private constructor() {

    companion object {

        const val USER_DATABASE = "users"
        const val FAVORITES_DATABASE = "favorites"
        const val SUBSCRIPTION_DATABASE = "subscriptions"

        private var instance: FirebaseRealtime? = null

        fun getInstance(): FirebaseRealtime {
            if (instance == null)
                instance = FirebaseRealtime()
            return instance!!
        }
    }

    private val database = FirebaseDatabase.getInstance("https://auracle-85d79-default-rtdb.asia-southeast1.firebasedatabase.app/").reference

    fun createUser(user: User): Task<Void> {
        return database.child(USER_DATABASE).child(user.uid).setValue(user)
    }

    fun getCurrentUserDetails(): Task<DataSnapshot>? {
        val currentUser = Authenticate.getInstance().user() ?: return null
        return database.child(USER_DATABASE).child(currentUser.uid).get()
    }

    fun addToFavorite(episode: ListenEpisodeShort): Task<Void>? {
        val currentUser = Authenticate.getInstance().user() ?: return null
        return database.child(FAVORITES_DATABASE).child(currentUser.uid).child(episode.id!!).setValue(episode)
    }

    fun getFavorites(): Task<DataSnapshot>? {
        val currentUser = Authenticate.getInstance().user() ?: return null
        return database.child(FAVORITES_DATABASE).child(currentUser.uid).get()
    }

    fun checkFavorite(id: String): Task<DataSnapshot>? {
        val currentUser = Authenticate.getInstance().user() ?: return null
        return database.child(FAVORITES_DATABASE).child(currentUser.uid).child(id).get()
    }

    fun removeFromFavorites(id: String): Task<Void>? {
        val currentUser = Authenticate.getInstance().user() ?: return null
        return database.child(FAVORITES_DATABASE).child(currentUser.uid).child(id).removeValue()
    }

    fun addToSubscription(listenSearchPodcast: ListenSearchPodcast): Task<Void>? {
        val currentUser = Authenticate.getInstance().user() ?: return null
        return database.child(SUBSCRIPTION_DATABASE).child(currentUser.uid).child(listenSearchPodcast.id!!).setValue(listenSearchPodcast)
    }

    fun getSubscriptions(): Task<DataSnapshot>? {
        val currentUser = Authenticate.getInstance().user() ?: return null
        return database.child(SUBSCRIPTION_DATABASE).child(currentUser.uid).get()
    }

    fun checkSubscription(id: String): Task<DataSnapshot>? {
        val currentUser = Authenticate.getInstance().user() ?: return null
        return database.child(SUBSCRIPTION_DATABASE).child(currentUser.uid).child(id).get()
    }

    fun removeFromSubscriptions(id: String): Task<Void>? {
        val currentUser = Authenticate.getInstance().user() ?: return null
        return database.child(SUBSCRIPTION_DATABASE).child(currentUser.uid).child(id).removeValue()
    }

}