package com.example.auracle

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.squareup.picasso.Picasso

class MusicService : Service() {
    private var myBinder = MyBinder()
    var mediaPlayer = MediaPlayer()
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var runnable: Runnable

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
    fun showNotification(playPauseBtn: Int) {
        ApplicationClass.createNotificationChannel(baseContext)
        val notification = createNotification(playPauseBtn)

        with(NotificationManagerCompat.from(baseContext)) {
            notify(13, notification.build())
        }

    }

    private fun createNotification(playPauseBtn: Int): NotificationCompat.Builder {

        val prevIntent =  Intent(baseContext, NotificationReceiver::class.java).setAction(ApplicationClass.PREVIOUS)
        val prevPendingIntent = PendingIntent.getBroadcast(baseContext, 0, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val playIntent =  Intent(baseContext, NotificationReceiver::class.java).setAction(ApplicationClass.PLAY)
        val playPendingIntent = PendingIntent.getBroadcast(baseContext, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val nextIntent =  Intent(baseContext, NotificationReceiver::class.java).setAction(ApplicationClass.NEXT)
        val nextPendingIntent = PendingIntent.getBroadcast(baseContext, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val intent = Intent(baseContext, Homepage::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(baseContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val mediaStyle = androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(1)

        val notification = NotificationCompat.Builder(baseContext, ApplicationClass.CHANNEL_ID)
            .setContentTitle(Player.podcastListPA[Player.podcastPosition].title)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .addAction(NotificationCompat.Action.Builder(R.drawable.previous, "Previous", prevPendingIntent).build())
            .addAction(NotificationCompat.Action.Builder(playPauseBtn, "Play", playPendingIntent).build())
            .addAction(NotificationCompat.Action.Builder(R.drawable.next_icon, "Next", nextPendingIntent).build())
            .setContentIntent(pendingIntent)
            .setStyle(mediaStyle)

        Picasso.get().load(Player.podcastListPA[Player.podcastPosition].thumbnail).into(object : com.squareup.picasso.Target {
            override fun onBitmapLoaded(bitmap: android.graphics.Bitmap?, from: Picasso.LoadedFrom?) {
                notification.setLargeIcon(bitmap)
            }

            override fun onBitmapFailed(e: java.lang.Exception?, errorDrawable: android.graphics.drawable.Drawable?) {
                notification.setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_launcher_foreground))
            }

            override fun onPrepareLoad(placeHolderDrawable: android.graphics.drawable.Drawable?) {
                notification.setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_launcher_foreground))
            }
        })
        return notification
    }

    fun seekBarSetup() {
        runnable = Runnable {
            val player = Player()
            val currentPosition = mediaPlayer!!.currentPosition.toLong()
            Player.binding.tvSeekbarStart.text = player.formatDuration(currentPosition)
            Player.binding.seekBarPA.progress = currentPosition.toInt()
            Handler(Looper.getMainLooper()).postDelayed(runnable, 1000)
        }
        Handler(Looper.getMainLooper()).postDelayed(runnable, 1000)
    }

}
