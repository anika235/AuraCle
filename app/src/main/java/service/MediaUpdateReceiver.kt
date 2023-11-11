package com.example.auracle.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.auracle.Player

class MediaUpdateReceiver() : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        when (intent.action) {
            Player.START_PLAYING -> {
                val roundaboutIntent = Intent(context, StreamService::class.java)
                roundaboutIntent.putExtra("action", "notification_action")
                roundaboutIntent.putExtra("notification_action", Player.START_PLAYING)
                context.startService(roundaboutIntent)
            }
            Player.NOTIFICATION_TOGGLE_PLAYING -> {
                val roundaboutIntent = Intent(context, StreamService::class.java)
                roundaboutIntent.putExtra("action", Player.TOGGLE_PLAYING)
                context.startService(roundaboutIntent)
            }
            Player.NOTIFICATION_PLAY_NEXT -> {
                val roundaboutIntent = Intent(context, StreamService::class.java)
                roundaboutIntent.putExtra("action", Player.PLAY_NEXT)
                context.startService(roundaboutIntent)
            }
            Player.NOTIFICATION_PLAY_PREVIOUS -> {
                val roundaboutIntent = Intent(context, StreamService::class.java)
                roundaboutIntent.putExtra("action", Player.PLAY_PREVIOUS)
                context.startService(roundaboutIntent)
            }
        }
    }

}