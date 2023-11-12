package com.example.auracle.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.auracle.PlayerFragment
import com.example.auracle.com.example.auracle.PlayerInterface

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        when (intent.action) {
            PlayerInterface.START_PLAYING -> {
                val roundaboutIntent = Intent(context, MusicService::class.java)
                roundaboutIntent.putExtra("action", "notification_action")
                roundaboutIntent.putExtra("notification_action", PlayerInterface.START_PLAYING)
                context.startService(roundaboutIntent)
            }
            PlayerInterface.NOTIFICATION_TOGGLE_PLAYING -> {
                val roundaboutIntent = Intent(context, MusicService::class.java)
                roundaboutIntent.putExtra("action", PlayerInterface.TOGGLE_PLAYING)
                context.startService(roundaboutIntent)
            }
            PlayerInterface.NOTIFICATION_PLAY_NEXT -> {
                val roundaboutIntent = Intent(context, MusicService::class.java)
                roundaboutIntent.putExtra("action", PlayerInterface.PLAY_NEXT)
                context.startService(roundaboutIntent)
            }
            PlayerInterface.NOTIFICATION_PLAY_PREVIOUS -> {
                val roundaboutIntent = Intent(context, MusicService::class.java)
                roundaboutIntent.putExtra("action", PlayerInterface.PLAY_PREVIOUS)
                context.startService(roundaboutIntent)
            }
        }
    }

}