package com.example.auracle.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.auracle.ApplicationClass
import com.example.auracle.Homepage
import com.example.auracle.R
import com.example.auracle.com.example.auracle.PlayerInterface
import com.squareup.picasso.Picasso

class MusicService : Service() {

    private var player: PlayerInterface? = null
    private val binder = LocalBinder()
    val mediaPlayer = MediaPlayer()
    private var title = ""
    private var thumbnail = ""
    private var looping = false
    private val updateTimeUi = object: Runnable {
        override fun run() {
            player?.updateTimeUI(mediaPlayer.currentPosition)
            Handler(Looper.getMainLooper()).postDelayed(this, 1000)
        }
    }
    private val handler = Handler(Looper.getMainLooper())

    inner class LocalBinder: Binder() {
        fun getService(): MusicService {
            // Return this instance of StreamService so clients can call public methods
            return this@MusicService
        }
    }

    override fun onCreate() {
        super.onCreate()
        mediaPlayer.reset()
        mediaPlayer.setOnCompletionListener {
            if (looping)
                mediaPlayer.seekTo(1)
            else
                player?.playNextEpisode()
        }
        mediaPlayer.setOnPreparedListener {
            player?.newEpisodePlayUI(0, mediaPlayer.duration)
            handler.post(updateTimeUi)
            mediaPlayer.start()
            showNotification(title, thumbnail)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        val action = intent?.getStringExtra("action")
        if (action == null)
            stopSelf()

        when (action) {
            "notification_action" -> {
                sendBroadcast(Intent(intent.getStringExtra("notification_action")))
            }
            PlayerInterface.START_PLAYING -> {
                val audio = intent.getStringExtra("audio")
                val title = intent.getStringExtra("title")
                val thumbnail = intent.getStringExtra("thumbnail")
                if (audio == null || title == null || thumbnail == null) {
                    stopSelf()
                    return START_STICKY
                }

                mediaPlayer.reset()
                mediaPlayer.setDataSource(audio)
                mediaPlayer.prepareAsync()

                this.title = title
                this.thumbnail = thumbnail
            }

            PlayerInterface.TOGGLE_PLAYING -> {
                if (mediaPlayer.isPlaying){
                    handler.removeCallbacks(updateTimeUi)
                    mediaPlayer.pause()
                }
                else {
                    handler.post(updateTimeUi)
                    mediaPlayer.start()
                }
                player?.togglePlayingUI()
                showNotification(title, thumbnail)
            }

            PlayerInterface.RESUME_PLAYING -> {
                player?.newEpisodePlayUI(mediaPlayer.currentPosition, mediaPlayer.duration)
            }

            PlayerInterface.PLAY_NEXT -> {
                player?.playNextEpisode()
            }

            PlayerInterface.PLAY_PREVIOUS -> {
                player?.playPreviousEpisode()
            }

            PlayerInterface.TOGGLE_LOOPING -> {
                looping = !looping
            }

            PlayerInterface.SEEK -> {
                val seekTo = intent.getIntExtra("seek", 0)
                mediaPlayer.seekTo(seekTo)
            }
        }

        return START_STICKY
    }

    @SuppressLint("MissingPermission")
    fun showNotification(title: String, thumbnail: String) {
        createNotificationChannel()
        val notification = createNotification(title, thumbnail)

        with(NotificationManagerCompat.from(baseContext)) {
            notify(13, notification.build())
        }

    }

    private fun createNotificationChannel() {

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(ApplicationClass.CHANNEL_ID, getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "AuraCle Channel"
            }

            // Register the channel with the system.
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(title: String, thumbnail: String): NotificationCompat.Builder {

        val intent = Intent(baseContext, Homepage::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(baseContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val mediaStyle = androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(1)

        val nxtIntent = Intent(baseContext, NotificationReceiver::class.java).setAction(PlayerInterface.NOTIFICATION_PLAY_NEXT)
        val nxtPendingIntent = PendingIntent.getBroadcast(baseContext, 0, nxtIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val nxtAction = NotificationCompat.Action.Builder(R.drawable.next_icon, "Next", nxtPendingIntent).build()

        val prevIntent = Intent(baseContext, NotificationReceiver::class.java).setAction(PlayerInterface.NOTIFICATION_PLAY_PREVIOUS)
        val prevPendingIntent = PendingIntent.getBroadcast(baseContext, 0, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val prevAction = NotificationCompat.Action.Builder(R.drawable.previous, "Next", prevPendingIntent).build()

        val togglePlayIntent = Intent(baseContext, NotificationReceiver::class.java).setAction(PlayerInterface.NOTIFICATION_TOGGLE_PLAYING)
        val togglePlayPendingIntent = PendingIntent.getBroadcast(baseContext, 0, togglePlayIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val togglePlayAction = NotificationCompat.Action.Builder(if(mediaPlayer.isPlaying) R.drawable.pause else R.drawable.play, "Next", togglePlayPendingIntent).build()

        val notification = NotificationCompat.Builder(baseContext, ApplicationClass.CHANNEL_ID)
            .setContentTitle(title)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .addAction(prevAction)
            .addAction(togglePlayAction)
            .addAction(nxtAction)
            .setContentIntent(pendingIntent)
            .setStyle(mediaStyle)

        Picasso.get().load(thumbnail).into(object : com.squareup.picasso.Target {
            override fun onBitmapLoaded(bitmap: android.graphics.Bitmap?,from: Picasso.LoadedFrom?) {
                notification.setLargeIcon(bitmap)
            }

            override fun onBitmapFailed(e: java.lang.Exception?,errorDrawable: android.graphics.drawable.Drawable?) {
                notification.setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_launcher_foreground))
            }

            override fun onPrepareLoad(placeHolderDrawable: android.graphics.drawable.Drawable?) {
                notification.setLargeIcon(BitmapFactory.decodeResource(resources,R.drawable.ic_launcher_foreground))
            }
        })

        return notification

    }

    fun setParent(player: PlayerInterface) {
        this.player = player
    }

    override fun onBind(p0: Intent?): IBinder {
        return binder
    }
}