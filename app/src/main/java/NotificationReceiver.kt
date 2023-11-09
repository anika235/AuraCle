package com.example.auracle

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import kotlin.system.exitProcess

class NotificationReceiver:BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when(intent?.action)
        {
            ApplicationClass.PREVIOUS -> Toast.makeText(context, "Previous Clicked", Toast.LENGTH_SHORT).show()
            ApplicationClass.PLAY -> if(Player.isPlaying) pausePodcast() else playPodcast()
            ApplicationClass.NEXT -> Toast.makeText(context, "Next Clicked", Toast.LENGTH_SHORT).show()
            ApplicationClass.EXIT -> {
                Player.musicService!!.stopForeground(true)
                Player.musicService = null
                exitProcess(1)
            }
        }
    }
    private fun playPodcast(){
        Player.isPlaying = true
        Player.musicService!!.mediaPlayer!!.start()
        Player.musicService!!.showNotification(R.drawable.pause)
        Player.binding.playPauseButton.setIconResource(R.drawable.pause)
    }
    private fun pausePodcast(){
        Player.isPlaying = false
        Player.musicService!!.mediaPlayer!!.pause()
        Player.musicService!!.showNotification(R.drawable.play)
        Player.binding.playPauseButton.setIconResource(R.drawable.play)
    }
    private fun prevNextPodcast(increment: Boolean, context: Context){
        val hello = Player()
        hello.setPodcastPosition(increment = increment)

    }


}