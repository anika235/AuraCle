package com.example.auracle.com.example.auracle.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.auracle.api.ListenNoteApi
import com.example.auracle.datapack.listennote.ListenPodcastLong
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel: ViewModel() {


    private val api = ListenNoteApi()

    private val mutablePlaylist = MutableLiveData<ListenPodcastLong>()
    val playlist: LiveData<ListenPodcastLong> get() = mutablePlaylist

    fun retrievePodcast(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val episodes = api.podcastDetails(id)
            withContext(Dispatchers.Main) {
                mutablePlaylist.value = episodes
            }
        }
    }

}