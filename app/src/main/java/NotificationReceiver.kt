package com.example.auracle

import android.app.Service.STOP_FOREGROUND_DETACH
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.squareup.picasso.Picasso
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.system.exitProcess

class NotificationReceiver:BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when(intent?.action)
        {
            ApplicationClass.PREVIOUS -> prevEpisode()
            ApplicationClass.PLAY -> if(Player.isPlaying) pausePodcast() else playPodcast()
            ApplicationClass.NEXT -> nextEpisode()
            ApplicationClass.EXIT -> {
                Player.musicService.stopForeground(STOP_FOREGROUND_DETACH)
                Player.musicService.onDestroy()
                exitProcess(1)
            }
        }
    }
    private fun playPodcast(){
        Player.isPlaying = true
        Player.musicService.mediaPlayer.start()
        Player.musicService.showNotification(R.drawable.pause)
        Player.binding.playPauseButton.setIconResource(R.drawable.pause)
    }
    private fun pausePodcast(){
        Player.isPlaying = false
        Player.musicService.mediaPlayer.pause()
        Player.musicService.showNotification(R.drawable.play)
        Player.binding.playPauseButton.setIconResource(R.drawable.play)
    }

    private fun setLayout() {
        Player.binding.podcastLoadingSkeleton.showSkeleton()
        Picasso.get().load(Player.podcastListPA[Player.podcastPosition].thumbnail).into(Player.binding.PodcastThumbnail)
        Player.binding.PodcastNamePA.text = Player.podcastListPA[Player.podcastPosition].title
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun createMediaPlayer() {

        Player.musicService.mediaPlayer.reset()

        GlobalScope.launch(Dispatchers.IO) {

            Player.musicService.mediaPlayer.setDataSource(Player.podcastListPA[Player.podcastPosition].audio)
            Player.musicService.mediaPlayer.prepare()

            withContext(Dispatchers.Main) {
                Player.binding.podcastLoadingSkeleton.showOriginal()
                Player.musicService.showNotification(R.drawable.pause)

                Player.binding.playPauseButton.setIconResource(R.drawable.pause)
                Player.musicService.showNotification(R.drawable.pause)
                Player.isPlaying = true
                Player.musicService.mediaPlayer.start()
            }
        }
    }

    private fun nextEpisode() {

        Player.podcastPosition = (Player.podcastPosition + 1) % Player.podcastListPA.size
        setLayout()
        createMediaPlayer()
    }

    private fun prevEpisode() {
        Player.podcastPosition = Math.floorMod(Player.podcastPosition - 1, Player.podcastListPA.size)
        setLayout()
        createMediaPlayer()
    }


}