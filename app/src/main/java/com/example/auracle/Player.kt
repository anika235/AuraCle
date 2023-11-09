package com.example.auracle

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.database.Observable
import android.media.MediaPlayer
import android.os.Bundle
import android.os.IBinder
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.auracle.databinding.ActivityPlayerBinding
import com.example.auracle.datapack.listennote.ListenEpisodeShort
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class Player : AppCompatActivity(), ServiceConnection {

    companion object {
        lateinit var podcastListPA: ArrayList<ListenEpisodeShort>
        var podcastPosition: Int = 0
        var isPlaying: Boolean = false
        var musicService = MusicService()
        lateinit var binding: ActivityPlayerBinding

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = Intent(this, MusicService::class.java)
        bindService(intent, this, BIND_AUTO_CREATE)
        startService(intent)

        initializeLayout()
        binding.playPauseButton.setOnClickListener {
            if (isPlaying) pausePodcast()
            else playPodcast()
        }
        binding.previousBtn.setOnClickListener { prevNextPodcast(increment = false) }
        binding.nextBtn.setOnClickListener { prevNextPodcast(increment = true) }
        binding.seekBarPA.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) musicService.mediaPlayer.seekTo(progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) = Unit
            override fun onStopTrackingTouch(p0: SeekBar?) = Unit

        })
    }

    private fun setLayout() {
        binding.podcastLoadingSkeleton.showSkeleton()
        Picasso.get().load(podcastListPA[podcastPosition].thumbnail).into(binding.PodcastThumbnail)
        binding.PodcastNamePA.text = podcastListPA[podcastPosition].title
    }

    private fun createMediaPlayer() {
        try {

            musicService.mediaPlayer.reset()
            val audioPath = podcastListPA[podcastPosition].audio

            lifecycleScope.launch(Dispatchers.IO) {
                musicService.mediaPlayer.setDataSource(audioPath)
                musicService.mediaPlayer.prepare()

                withContext(Dispatchers.Main) {
                    binding.podcastLoadingSkeleton.showOriginal()

                    playPodcast()

                    binding.tvSeekbarStart.text = formatDuration(musicService.mediaPlayer.currentPosition.toLong())
                    binding.tvSeekbarEnd.text = formatDuration(musicService.mediaPlayer.duration.toLong())
                    binding.seekBarPA.progress = 0
                    binding.seekBarPA.max = musicService.mediaPlayer.duration
                }

            }
        } catch (e: Exception) {
            return
        }
    }

    private fun initializeLayout() {
        podcastPosition = intent.getIntExtra("index", 0)
        when (intent.getStringExtra("class")) {
            "EpisodeCardAdapter" -> {
                podcastListPA = ArrayList()
                podcastListPA.addAll(PodcastDetailsFragment.episodes)
                setLayout()
            }
        }
    }

    private fun playPodcast() {
        binding.playPauseButton.setIconResource(R.drawable.pause)
        musicService.showNotification(R.drawable.pause)
        isPlaying = true
        musicService.mediaPlayer.start()
    }

    private fun pausePodcast() {
        binding.playPauseButton.setIconResource(R.drawable.play)
        musicService.showNotification(R.drawable.play)
        isPlaying = false
        musicService.mediaPlayer.pause()
    }

    private fun prevNextPodcast(increment: Boolean) {
        if (increment) {
            setPodcastPosition(increment = true)
            setLayout()
            createMediaPlayer()
        } else {
            setPodcastPosition(increment = false)
            setLayout()
            createMediaPlayer()
        }
    }

    private fun setPodcastPosition(increment: Boolean) {
        if (increment) {
            if (podcastListPA.size - 1 == podcastPosition)
                podcastPosition = 0
            else
                ++podcastPosition
        } else {
            if (0 == podcastPosition)
                podcastPosition = podcastListPA.size - 1
            else
                --podcastPosition
        }
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = service as MusicService.MyBinder
        musicService = binder.currenServise()
        createMediaPlayer()
//        musicService.showNotification(R.drawable.pause)
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        musicService.onDestroy()
    }
    private fun formatDuration(duration: Long): String {
        val minutes = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
        val seconds = TimeUnit.SECONDS.convert(duration, TimeUnit.MILLISECONDS) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }


}