package com.example.auracle

import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.auracle.databinding.ActivityPlayerBinding
import com.example.auracle.datapack.listennote.ListenEpisodeShort

class player : AppCompatActivity() {

    companion object{
        lateinit var podcastlistPA: ArrayList<ListenEpisodeShort>
        var podcastPosition: Int = 0
        var mediaPlayer: MediaPlayer? = null

    }
    private lateinit var podcastId: String
    private lateinit var binding: ActivityPlayerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        podcastPosition = intent.getIntExtra("index", 0)
        when (intent.getStringExtra("class")){
            "EpisodeCardAdapter" ->{
                podcastlistPA = ArrayList()
                podcastlistPA.addAll(PodcastDetails.episodes)
                if(mediaPlayer == null) mediaPlayer = MediaPlayer()
                mediaPlayer!!.reset()
                val audioPath = podcastlistPA[podcastPosition].audio
                mediaPlayer!!.setDataSource(audioPath)
                mediaPlayer!!.prepare()
                mediaPlayer!!.start()

            }

        }

    }


}