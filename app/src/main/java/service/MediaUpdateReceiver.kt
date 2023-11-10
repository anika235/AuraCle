package com.example.auracle.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.auracle.Player

class MediaUpdateReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.w("HEREWHAY", "Received")
        when (intent?.action) {
            Player.START_PLAYING -> {
                Log.w("OOKY", "Start playing")
            }
//                STOP_PLAYING -> pauseEpisode()
//                PLAY_NEXT -> playlistViewModel.toNextEpisode()
//                PLAY_PREVIOUS -> playlistViewModel.toPreviousEpisode()
        }
    }

}