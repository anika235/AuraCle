package com.example.auracle

import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.auracle.api.ListenNoteApi
import com.example.auracle.databinding.ActivityPlayerBinding
import com.example.auracle.datapack.listennote.ListenEpisodeShort
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Player : AppCompatActivity() {

    companion object{
        lateinit var podcastlistPA: ArrayList<ListenEpisodeShort>
        var podcastPosition: Int = 0
        val mediaPlayer = MediaPlayer()
        var isPlaying:Boolean = false

    }
//    private lateinit var podcastId: String
    private lateinit var binding: ActivityPlayerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeLayout()
        binding.playPauseButton.setOnClickListener{
            if(isPlaying) pausePodcast()
            else playPodcast()

        }
    }
    private fun setLayout()
    {
//        val podcastDetails = ListenNoteApi().podcastDetails(podcastId)

        binding.podcastLoadingSkeleton.showSkeleton()
        Picasso.get().load(podcastlistPA[podcastPosition].thumbnail).into(binding.PodcastThumbnail)
        binding.PodcastNamePA.text = podcastlistPA[podcastPosition].title
    }
    private fun createMediaPlayer()
    {
        try {

            mediaPlayer.reset()
            val audioPath = podcastlistPA[podcastPosition].audio

            lifecycleScope.launch(Dispatchers.IO) {
                mediaPlayer.setDataSource(audioPath)
                mediaPlayer.prepare()

                withContext(Dispatchers.Main) {
                    binding.podcastLoadingSkeleton.showOriginal()
                    playPodcast()
                }

            }
        }
        catch (e: Exception) {
            return
        }
    }
    private fun initializeLayout()
    {
        podcastPosition = intent.getIntExtra("index", 0)
        when (intent.getStringExtra("class")){
            "EpisodeCardAdapter" ->{
                podcastlistPA = ArrayList()
                podcastlistPA.addAll(PodcastDetails.episodes)
                setLayout()
                createMediaPlayer()
            }
        }
    }
    private fun playPodcast()
    {
        binding.playPauseButton.setIconResource(R.drawable.pause)
        isPlaying = true
        mediaPlayer.start()

    }

    private fun pausePodcast()
    {
        binding.playPauseButton.setIconResource(R.drawable.play)
        isPlaying = false
        mediaPlayer.pause()

    }

}