package com.example.auracle

import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat

class MusicService: Service() {
    private var myBinder = MyBinder()
    var mediaPlayer: MediaPlayer? = null
    private lateinit var mediaSession: MediaSessionCompat


    override fun onBind(p0: Intent?): IBinder? {
        mediaSession = MediaSessionCompat(baseContext, "My Podcast")
        return myBinder
    }
    inner class MyBinder: Binder(){
        fun currenServise(): MusicService {
            return this@MusicService
        }
    }
    fun  showNotification(){
        val notification = NotificationCompat.Builder(baseContext, ApplicationClass.CHANNEL_ID)
            .setContentTitle(Player.podcastlistPA[Player.podcastPosition].title)
            .setSmallIcon(R.drawable.podcast)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.icon))
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(mediaSession.sessionToken))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .addAction(R.drawable.previous, "Previous", null)
            .addAction(R.drawable.play, "Play", null)
            .addAction(R.drawable.next_icon, "Next", null)
            .addAction(R.drawable.close, "Exit", null)
            .build()

        startForeground(13, notification)

    }
}