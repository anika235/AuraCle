package com.example.auracle

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.auracle.com.example.auracle.dataholder.PlaylistDataHolder
import com.example.auracle.com.example.auracle.viewmodel.PlaylistViewModel
import com.example.auracle.databinding.ActivityPlayerBinding
import com.example.auracle.datapack.listennote.ListenEpisodeShort
import com.example.auracle.service.StreamService

class Player : AppCompatActivity() {

    companion object {
        const val START_PLAYING = "start_playing"
        const val STOP_PLAYING = "stop_playing"
        const val PLAY_NEXT = "play_next"
        const val PLAY_PREVIOUS = "play_previous"
    }

    private lateinit var binding: ActivityPlayerBinding
    private val playlistViewModel: PlaylistViewModel by viewModels()
    private var isPlaying = false
    private var buttonMask = true  // buttonMask should mask the actions of player buttons

    private val mediaUpdateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                START_PLAYING -> {

                    buttonMask = false
                    isPlaying = true

                    binding.podcastLoadingSkeleton.showOriginal()
                    binding.playPauseButton.setIconResource(R.drawable.pause)
                }
//                STOP_PLAYING -> {
//                    binding.podcastLoadingSkeleton.showSkeleton()
//                    binding.playPauseButton.setIconResource(R.drawable.play)
//                }
                PLAY_NEXT -> {

                    Log.w("PlayerTTT", "Play next")

                    binding.podcastLoadingSkeleton.showSkeleton()
                    binding.playPauseButton.setIconResource(R.drawable.pause)
                    playNextEpisode()
                }
                PLAY_PREVIOUS -> {

                    Log.w("PlayerTTT", "Play next")


                    binding.podcastLoadingSkeleton.showSkeleton()
                    binding.playPauseButton.setIconResource(R.drawable.pause)
                    playPreviousEpisode()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buttonMask = true
        val bundle = intent.getBundleExtra("baseInfo")
        val episodeList = bundle?.getParcelableArrayList<ListenEpisodeShort>("episodeList")
        val index = bundle?.getInt("index")
        val source = bundle?.getString("class")

        try {
            if (source == "EpisodeCardAdapter") {
                playlistViewModel.initPlaylist(episodeList!!, index!!)
            } else {
                val playlistDataHolder = PlaylistDataHolder.getInstance()
                playlistViewModel.initPlaylist()
            }
        } catch (e: Exception) {
            Log.d("Player", "Error: $e")
            finish()
        }

        registerButtons()
        registerBroadcast()
        playNewEpisode()
    }

    private fun registerButtons() {
        binding.playPauseButton.setOnClickListener {
            if (!buttonMask) {

                if (isPlaying)
                    binding.playPauseButton.setIconResource(R.drawable.play)
                else
                    binding.playPauseButton.setIconResource(R.drawable.pause)

                val togglePlayIntent = Intent(this, StreamService::class.java)
                togglePlayIntent.putExtra("action", "togglePlay")
                startService(togglePlayIntent)

            }
        }

        binding.previousBtn.setOnClickListener {
            if (!buttonMask) {
                playPreviousEpisode()
            }
        }

        binding.nextBtn.setOnClickListener {
            if (!buttonMask) {
                playNextEpisode()
            }
        }
    }

    private fun registerBroadcast() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(START_PLAYING)
        intentFilter.addAction(STOP_PLAYING)
        intentFilter.addAction(PLAY_NEXT)
        intentFilter.addAction(PLAY_PREVIOUS)
        LocalBroadcastManager.getInstance(this).registerReceiver(mediaUpdateReceiver, intentFilter)
    }

    private fun playNewEpisode() {

        buttonMask = true
        isPlaying = false

        binding.podcastLoadingSkeleton.showSkeleton()
        binding.playPauseButton.setIconResource(R.drawable.play)
        val playIntent = Intent(this, StreamService::class.java)
        playIntent.putExtra("action", "playNew")

        val episode = playlistViewModel.getEpisode()
        playIntent.putExtra("audio", episode.audio)
        playIntent.putExtra("title", episode.title)
        playIntent.putExtra("thumbnail", episode.thumbnail)
        startService(playIntent)
    }

    private fun playNextEpisode() {
        playlistViewModel.toNextEpisode()
        playNewEpisode()
    }

    private fun playPreviousEpisode() {
        playlistViewModel.toPreviousEpisode()
        playNewEpisode()
    }

//    companion object {
//        lateinit var podcastListPA: ArrayList<ListenEpisodeShort>
//        var podcastPosition: Int = 0
//        var isPlaying: Boolean = false
//        var musicService = MusicService()
//        lateinit var binding: ActivityPlayerBinding
//        var repeat: Boolean = false
//
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        binding = ActivityPlayerBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        val intent = Intent(this, MusicService::class.java)
//        bindService(intent, this, BIND_AUTO_CREATE)
//        startService(intent)
//        initializeLayout()
//
//        binding.backButtonPA.setOnClickListener { finish() }
//
//
//        binding.repeatButtonPA.setOnClickListener {
//            if(!repeat){
//                repeat = true
//                binding.repeatButtonPA.setColorFilter(ContextCompat.getColor(this, R.color.purple))
//            }
//            else{
//                repeat = false
//                binding.repeatButtonPA.setColorFilter(ContextCompat.getColor(this, R.color.pink))
//            }
//        }
//    }
//
//
//    private fun setLayout() {
//        binding.podcastLoadingSkeleton.showSkeleton()
//        Picasso.get().load(podcastListPA[podcastPosition].thumbnail).into(binding.PodcastThumbnail)
//        binding.PodcastNamePA.text = podcastListPA[podcastPosition].title
//        if(repeat)  binding.repeatButtonPA.setColorFilter(ContextCompat.getColor(this, R.color.purple))
//
//    }
//
//    private fun createMediaPlayer() {
//        try {
//
//            musicService.mediaPlayer.reset()
//            val audioPath = podcastListPA[podcastPosition].audio
//
//            lifecycleScope.launch(Dispatchers.IO) {
//                musicService.mediaPlayer.setDataSource(audioPath)
//                musicService.mediaPlayer.prepare()
//
//                withContext(Dispatchers.Main) {
//                    binding.playPauseButton.setOnClickListener {
//                        if (isPlaying) pausePodcast()
//                        else playPodcast()
//                    }
//                    binding.previousBtn.setOnClickListener { prevNextPodcast(increment = false) }
//                    binding.nextBtn.setOnClickListener { prevNextPodcast(increment = true) }
//
//                    binding.seekBarPA.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
//                        override fun onProgressChanged(p0: SeekBar?, progress: Int, fromUser: Boolean) {
//                            if(fromUser) musicService.mediaPlayer.seekTo(progress)
//                        }
//
//                        override fun onStartTrackingTouch(p0: SeekBar?) = Unit
//                        override fun onStopTrackingTouch(p0: SeekBar?) = Unit
//
//                    })
//
//                    binding.podcastLoadingSkeleton.showOriginal()
//
//                    playPodcast()
//
//                    binding.tvSeekbarStart.text = formatDuration(musicService.mediaPlayer.currentPosition.toLong())
//                    binding.tvSeekbarEnd.text = formatDuration(musicService.mediaPlayer.duration.toLong())
//                    binding.seekBarPA.progress = 0
//                    binding.seekBarPA.max = musicService.mediaPlayer.duration
//
//                    binding.ShareButttonPA.setOnClickListener {
//                        val shareIntent = Intent()
//                        shareIntent.action = Intent.ACTION_SEND
//                        shareIntent.type = "audio/*"
//                        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(audioPath))
//                        startActivity(Intent.createChooser(shareIntent,"Sharing the Podcast"))
//                    }
//
//                    musicService!!.mediaPlayer!!.setOnCompletionListener {prevNextPodcast(true)}
//                }
//
//            }
//        } catch (e: Exception) {
//            return
//        }
//    }
//
//    private fun initializeLayout() {
//        podcastPosition = intent.getIntExtra("index", 0)
//        when (intent.getStringExtra("class")) {
//            "EpisodeCardAdapter" -> {
//                podcastListPA = ArrayList()
//                podcastListPA.addAll(PodcastDetailsFragment.episodes)
//                setLayout()
//            }
//        }
//    }
//
//    private fun playPodcast() {
//        binding.playPauseButton.setIconResource(R.drawable.pause)
//        musicService.showNotification(R.drawable.pause)
//        isPlaying = true
//        musicService.mediaPlayer.start()
//    }
//
//    private fun pausePodcast() {
//        binding.playPauseButton.setIconResource(R.drawable.play)
//        musicService.showNotification(R.drawable.play)
//        isPlaying = false
//        musicService.mediaPlayer.pause()
//    }
//
//    private fun prevNextPodcast(increment: Boolean) {
//        if (increment) {
//            setPodcastPosition(increment = true)
//            setLayout()
//            createMediaPlayer()
//        } else {
//            setPodcastPosition(increment = false)
//            setLayout()
//            createMediaPlayer()
//        }
//    }
//
//    private fun setPodcastPosition(increment: Boolean) {
//       if(!repeat){
//           if (increment) {
//               if (podcastListPA.size - 1 == podcastPosition)
//                   podcastPosition = 0
//               else
//                   ++podcastPosition
//           } else {
//               if (0 == podcastPosition)
//                   podcastPosition = podcastListPA.size - 1
//               else
//                   --podcastPosition
//           }
//       }
//    }
//
//    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
//        val binder = service as MusicService.MyBinder
//        musicService = binder.currentService()
//        createMediaPlayer()
//        musicService!!.seekBarSetup()
//        binding = ActivityPlayerBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//    }
//
//    override fun onServiceDisconnected(name: ComponentName?) {
//        musicService.onDestroy()
//    }
//    fun formatDuration(duration: Long): String {
//        val minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
//        val seconds = TimeUnit.MILLISECONDS.toSeconds(duration) % 60
//        return String.format("%02d:%02d", minutes, seconds)
//    }
//

}