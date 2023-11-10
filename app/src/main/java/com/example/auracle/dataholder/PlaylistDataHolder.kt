package com.example.auracle.com.example.auracle.dataholder

import com.example.auracle.datapack.listennote.ListenEpisodeShort

class PlaylistDataHolder {
    private var episodeList: ArrayList<ListenEpisodeShort>? = null
    private var episodePosition: Int = 0
    private var episodeDataAvailable: Boolean = false

    fun initPlaylist(playlist: ArrayList<ListenEpisodeShort>, position: Int) {
        episodeList = playlist
        episodePosition = position
        episodeDataAvailable = true
    }

    fun getPlaylist(): ArrayList<ListenEpisodeShort>? {
        return episodeList
    }

    fun getPlaylistPosition(): Int {
        return episodePosition
    }

    companion object {
        private var playlistDataHolder = PlaylistDataHolder()

        fun getInstance(): PlaylistDataHolder {
            return playlistDataHolder
        }
    }


}