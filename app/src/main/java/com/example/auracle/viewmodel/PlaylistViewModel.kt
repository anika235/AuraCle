package com.example.auracle.com.example.auracle.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.auracle.api.ListenNoteApi
import com.example.auracle.datapack.listennote.ListenEpisodeShort

class PlaylistViewModel : ViewModel() {

    private val api = ListenNoteApi()

//    private val mutablePlaylist = MutableLiveData<ArrayList<ListenEpisodeShort>>()
//    val playlist: LiveData<ArrayList<ListenEpisodeShort>> get() = mutablePlaylist

    private val mutableNowPlaying = MutableLiveData<Boolean>()
    val nowPlaying: LiveData<Boolean> get() = mutableNowPlaying

    var playlistPosition: Int = 0
    var playList: ArrayList<ListenEpisodeShort> = ArrayList()
    private var playlistAvailable: Boolean = false


    fun initPlaylist(playlist: ArrayList<ListenEpisodeShort>, position: Int) {
        playList = playlist
        playlistPosition = position
        playlistAvailable = true
    }

    fun isPlaylistInitialized(): Boolean {
        return playlistAvailable
    }

    fun getEpisode(): ListenEpisodeShort {
        return playList[playlistPosition]
    }

    fun toNextEpisode() {
        if (playlistAvailable) {
            playlistPosition = (playlistPosition + 1) % playList.size
        }
    }

    fun toPreviousEpisode() {
        if (playlistAvailable) {
            playlistPosition = Math.floorMod(playlistPosition - 1, playList.size)
        }
    }

    fun showNowPlaying() {
        mutableNowPlaying.value = true
    }

    fun hideNowPlaying() {
        mutableNowPlaying.value = false
    }

}