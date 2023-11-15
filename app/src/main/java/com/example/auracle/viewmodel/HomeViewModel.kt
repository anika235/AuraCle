package com.example.auracle.com.example.auracle.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.auracle.api.ListenNoteApi
import com.example.auracle.com.example.auracle.api.roomapi.EpisodeDao
import com.example.auracle.com.example.auracle.datapack.room.RoomEpisode
import com.example.auracle.datapack.listennote.ListenPodcastLong
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel: ViewModel() {


    private val api = ListenNoteApi()

    var playlist: ListenPodcastLong = ListenPodcastLong()
    var offlineAvailableList: ArrayList<RoomEpisode> = ArrayList()

//    private val mutablePlaylist = MutableLiveData<ListenPodcastLong>()
//    val playlist: LiveData<ListenPodcastLong> get() = mutablePlaylist

    private val mutablePlaylistAvailable = MutableLiveData<Boolean>()
    val playlistAvailable: LiveData<Boolean> get() = mutablePlaylistAvailable
    private val mutableOfflineAvailable = MutableLiveData<Boolean>()
    val offlineAvailable: LiveData<Boolean> get() = mutableOfflineAvailable


    fun retrievePodcast(podcastId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val episodes = api.podcastDetails(podcastId)
            withContext(Dispatchers.Main) {
                playlist = episodes
                mutablePlaylistAvailable.value = true
            }
        }
    }

    fun retrieveOfflinePodcast(podcastId: String, episodeDao: EpisodeDao) {
        viewModelScope.launch(Dispatchers.IO) {
            val offlineList = episodeDao.getPodcast(podcastId)
            withContext(Dispatchers.Main) {
                offlineAvailableList = offlineList as ArrayList<RoomEpisode>
                mutableOfflineAvailable.value = true
            }
        }
    }

    fun retrieveOffline(episodeDao: EpisodeDao) {

        mutableOfflineAvailable.value = false

        Log.w("thing", "retrieving ofline")

        viewModelScope.launch(Dispatchers.IO) {
            val offlineList = episodeDao.getAll()
            withContext(Dispatchers.Main) {
                Log.w("thing", "retrieved ofline: $offlineList")
                offlineAvailableList = offlineList as ArrayList<RoomEpisode>
                mutableOfflineAvailable.value = true
            }
        }
    }

}