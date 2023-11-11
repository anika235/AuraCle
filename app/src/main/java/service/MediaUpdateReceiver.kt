package com.example.auracle.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.auracle.PlayerFragment

class MediaUpdateReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        when (intent.action) {
            PlayerFragment.START_PLAYING -> {
                val roundaboutIntent = Intent(context, MusicService::class.java)
                roundaboutIntent.putExtra("action", "notification_action")
                roundaboutIntent.putExtra("notification_action", PlayerFragment.START_PLAYING)
                context.startService(roundaboutIntent)
            }
            PlayerFragment.NOTIFICATION_TOGGLE_PLAYING -> {
                val roundaboutIntent = Intent(context, MusicService::class.java)
                roundaboutIntent.putExtra("action", PlayerFragment.TOGGLE_PLAYING)
                context.startService(roundaboutIntent)
            }
            PlayerFragment.NOTIFICATION_PLAY_NEXT -> {
                val roundaboutIntent = Intent(context, MusicService::class.java)
                roundaboutIntent.putExtra("action", PlayerFragment.PLAY_NEXT)
                context.startService(roundaboutIntent)
            }
            PlayerFragment.NOTIFICATION_PLAY_PREVIOUS -> {
                val roundaboutIntent = Intent(context, MusicService::class.java)
                roundaboutIntent.putExtra("action", PlayerFragment.PLAY_PREVIOUS)
                context.startService(roundaboutIntent)
            }
        }
    }

}