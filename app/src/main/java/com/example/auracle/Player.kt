package com.example.auracle

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.auracle.databinding.ActivityPlayerBinding
import com.example.auracle.datapack.listennote.ListenEpisodeShort
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Player : AppCompatActivity(), ServiceConnection {

    companion object {
        lateinit var podcastListPA: ArrayList<ListenEpisodeShort>
        var podcastPosition: Int = 0
        var isPlaying: Boolean = false
        var musicService: MusicService? = null

    }

    private lateinit var binding: ActivityPlayerBinding
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
//        binding.seekBarPA.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
//            override fun onProgressChanged(p0: SeekBar?, progress: Int, fromUser: Boolean) {
//                if(fromUser)
//            }
//
//            override fun onStartTrackingTouch(p0: SeekBar?) = Unit
//            override fun onStopTrackingTouch(p0: SeekBar?) = Unit
//
//        })
    }

    private fun setLayout() {
        binding.podcastLoadingSkeleton.showSkeleton()
        Picasso.get().load(podcastListPA[podcastPosition].thumbnail).into(binding.PodcastThumbnail)
        binding.PodcastNamePA.text = podcastListPA[podcastPosition].title
    }

    private fun createMediaPlayer() {
        try {
            if (musicService!!.mediaPlayer == null) musicService!!.mediaPlayer = MediaPlayer()

            musicService!!.mediaPlayer!!.reset()
            val audioPath = podcastListPA[podcastPosition].audio

            lifecycleScope.launch(Dispatchers.IO) {
                musicService!!.mediaPlayer!!.setDataSource(audioPath)
                musicService!!.mediaPlayer!!.prepare()

                withContext(Dispatchers.Main) {
                    binding.podcastLoadingSkeleton.showOriginal()
                    musicService!!.showNotification()
                    playPodcast()
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
                podcastListPA.addAll(PodcastDetails.episodes)
                setLayout()
            }
        }
    }

    private fun playPodcast() {
        binding.playPauseButton.setIconResource(R.drawable.pause)
        isPlaying = true
        musicService!!.mediaPlayer!!.start()
    }

    private fun pausePodcast() {
        binding.playPauseButton.setIconResource(R.drawable.play)
        isPlaying = false
        musicService!!.mediaPlayer!!.pause()
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
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        musicService = null
    }

}