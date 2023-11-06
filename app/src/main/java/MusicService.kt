package com.example.auracle

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MusicService : Service() {
    private var myBinder = MyBinder()
    var mediaPlayer: MediaPlayer? = null
    private lateinit var mediaSession: MediaSessionCompat

    override fun onBind(p0: Intent?): IBinder? {
        mediaSession = MediaSessionCompat(baseContext, "My Podcast")
        return myBinder
    }

    inner class MyBinder : Binder() {
        fun currenServise(): MusicService {
            return this@MusicService
        }
    }

    @SuppressLint("MissingPermission")
    fun showNotification() {
        ApplicationClass.createNotificationChannel(baseContext)
        val notification = createNotification()

        with(NotificationManagerCompat.from(baseContext)) {
            notify(13, notification.build())
        }

    }

    private fun createNotification(): NotificationCompat.Builder {

        val intent = Intent(baseContext, Homepage::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(baseContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val mediaStyle = androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(1)

        return NotificationCompat.Builder(baseContext, ApplicationClass.CHANNEL_ID)
            .setContentTitle(Player.podcastListPA[Player.podcastPosition].title)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_launcher_foreground))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .addAction(NotificationCompat.Action.Builder(R.drawable.previous, "Previous", null).build())
            .addAction(NotificationCompat.Action.Builder(R.drawable.play, "Play", null).build())
            .addAction(NotificationCompat.Action.Builder(R.drawable.next_icon, "Next", null).build())
            .addAction(NotificationCompat.Action.Builder(R.drawable.close, "Exit", null).build())
            .setContentIntent(pendingIntent)
            .setStyle(mediaStyle)

        mediaSession.setMediaButtonReceiver(pendingIntent)
    }
}
