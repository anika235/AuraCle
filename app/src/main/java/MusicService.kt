package com.example.auracle

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.squareup.picasso.Picasso
import java.net.URL

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
    fun showNotification(playPauseBtn: Int) {
        ApplicationClass.createNotificationChannel(baseContext)
        val notification = createNotification(playPauseBtn)

        with(NotificationManagerCompat.from(baseContext)) {
            notify(13, notification.build())
        }

    }

    private fun createNotification(playPauseBtn: Int): NotificationCompat.Builder {

        val PrevIntent =  Intent(baseContext, NotificationReceiver::class.java).setAction(ApplicationClass.PREVIOUS)
        val prevpendingIntent = PendingIntent.getBroadcast(baseContext, 0, PrevIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val PlayIntent =  Intent(baseContext, NotificationReceiver::class.java).setAction(ApplicationClass.PLAY)
        val playpendingIntent = PendingIntent.getBroadcast(baseContext, 0, PlayIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val nextIntent =  Intent(baseContext, NotificationReceiver::class.java).setAction(ApplicationClass.NEXT)
        val nextpendingIntent = PendingIntent.getBroadcast(baseContext, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val exitIntent =  Intent(baseContext, NotificationReceiver::class.java).setAction(ApplicationClass.EXIT)
        val exitpendingIntent = PendingIntent.getBroadcast(baseContext, 0, exitIntent, PendingIntent.FLAG_UPDATE_CURRENT)

//        val url = URL(Player.podcastListPA[Player.podcastPosition].thumbnail)
//        val image = url.readBytes()
//        BitmapFactory.decodeByteArray(image, 0, image.size)
//
//        val largeIcon = if (image != null) {
//            BitmapFactory.decodeByteArray(image, 0, image.size)
//        } else {
//            BitmapFactory.decodeResource(resources, R.drawable.ic_launcher_foreground)
//        }

        val intent = Intent(baseContext, Homepage::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(baseContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val mediaStyle = androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(1)
        //CoroutineScope(Dispatchers.IO).launch {

//            withContext(Dispatchers.Main){
//
//            }
//

        //}

        val notification = NotificationCompat.Builder(baseContext, ApplicationClass.CHANNEL_ID)
            .setContentTitle(Player.podcastListPA[Player.podcastPosition].title)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
//            .setLargeIcon(largeIcon)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .addAction(NotificationCompat.Action.Builder(R.drawable.previous, "Previous", prevpendingIntent).build())
            .addAction(NotificationCompat.Action.Builder(playPauseBtn, "Play", playpendingIntent).build())
            .addAction(NotificationCompat.Action.Builder(R.drawable.next_icon, "Next", nextpendingIntent).build())
            .addAction(NotificationCompat.Action.Builder(R.drawable.close, "Exit", exitpendingIntent).build())
            .setContentIntent(pendingIntent)
            .setStyle(mediaStyle)
        Picasso.get().load(Player.podcastListPA[Player.podcastPosition].thumbnail).into(Target(){
            @Override

        } )

        mediaSession.setMediaButtonReceiver(pendingIntent)
    }

    fun getImage(path: String?): ByteArray? {
        Log.w("no work", path.toString() )
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(path)
        return retriever.embeddedPicture
    }
}
