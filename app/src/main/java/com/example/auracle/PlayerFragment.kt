package com.example.auracle

import android.content.ComponentName
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.auracle.com.example.auracle.viewmodel.PlaylistViewModel
import com.example.auracle.databinding.FragmentPlayerBinding
import com.example.auracle.datapack.listennote.ListenEpisodeShort
import com.example.auracle.service.MediaUpdateReceiver
import com.example.auracle.service.MusicService
import com.squareup.picasso.Picasso
import java.util.concurrent.TimeUnit

class PlayerFragment : Fragment() {
    companion object {
        const val START_PLAYING = "start_playing"
        const val STOP_PLAYING = "stop_playing"
        const val PLAY_NEXT = "play_next"
        const val PLAY_PREVIOUS = "play_previous"
        const val TOGGLE_PLAYING = "toggle_playing"
        const val TOGGLE_LOOPING = "toggle_looping"
        const val SEEK = "seek"
        const val NOTIFICATION_START_PLAYING = "notification_start_playing"
        const val NOTIFICATION_STOP_PLAYING = "notification_stop_playing"
        const val NOTIFICATION_PLAY_NEXT = "notification_play_next"
        const val NOTIFICATION_PLAY_PREVIOUS = "notification_play_previous"
        const val NOTIFICATION_TOGGLE_PLAYING = "notification_toggle_playing"
    }

    private var musicService: MusicService? = null
    private lateinit var binding: FragmentPlayerBinding
    private val playlistViewModel: PlaylistViewModel by activityViewModels()
    private var isPlaying = false
    private var isLooping = false
    private var buttonMask = true  // buttonMask should mask the actions of player buttons

    private val mediaUpdateReceiver = MediaUpdateReceiver()
    private val serviceConnection = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as MusicService.LocalBinder
            musicService = binder.getService()
            musicService!!.setParent(this@PlayerFragment)
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            return
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val episodeList = it.getParcelableArrayList<ListenEpisodeShort>("episodeList")
            val index = it.getInt("index")
            val source = it.getString("class")

            try {
                if (source == "PodcastDetailsFragment")
                    playlistViewModel.initPlaylist(episodeList!!, index) else TODO()

            } catch (e: Exception) {
                Log.d("PlayerFragment", "Error: $e")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        requireActivity().bindService(Intent(requireContext(), MusicService::class.java), serviceConnection,
            AppCompatActivity.BIND_AUTO_CREATE
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayerBinding.inflate(layoutInflater)

        buttonMask = true
        registerInteractive()
        registerBroadcast()
        playNewEpisode()

        return binding.root
    }

    private fun registerInteractive() {
        binding.playPauseButton.setOnClickListener {
            if (!buttonMask) {
                val togglePlayIntent = Intent(requireActivity(), MusicService::class.java)
                togglePlayIntent.putExtra("action", TOGGLE_PLAYING)
                requireContext().startService(togglePlayIntent)
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

        binding.repeatButtonPA.setOnClickListener {
//            if (!buttonMask) {}
            isLooping = !isLooping
            binding.repeatButtonPA.setColorFilter(if(isLooping) ContextCompat.getColor(requireContext(), R.color.purple) else ContextCompat.getColor(requireContext(), R.color.pink))
            val intent = Intent(requireActivity(), MusicService::class.java)
            intent.putExtra("action", TOGGLE_LOOPING)
            requireContext().startService(intent)
        }

        binding.ShareButtonPA.setOnClickListener {
            if (!buttonMask) {
                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.type = "audio/*"
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(playlistViewModel.getEpisode().audio))
                startActivity(Intent.createChooser(shareIntent,"Sharing the Podcast"))
            }
        }

        binding.seekBarPA.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val intent = Intent(requireActivity(), MusicService::class.java)
                    intent.putExtra("action", SEEK)
                    intent.putExtra("seek", progress)
                    requireContext().startService(intent)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
            override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
        })

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun registerBroadcast() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(START_PLAYING)
//        intentFilter.addAction(STOP_PLAYING)
        intentFilter.addAction(PLAY_NEXT)
        intentFilter.addAction(PLAY_PREVIOUS)
        requireContext().registerReceiver(mediaUpdateReceiver, intentFilter, AppCompatActivity.RECEIVER_EXPORTED)
    }

    private fun playNewEpisode() {

        buttonMask = true
        isPlaying = false

        binding.podcastLoadingSkeleton.showSkeleton()
        binding.playPauseButton.setIconResource(R.drawable.play)
        Picasso.get().load(playlistViewModel.getEpisode().thumbnail).into(binding.PodcastThumbnail)
        binding.EpisodeNamePA.text = playlistViewModel.getEpisode().title
        if(isLooping) binding.repeatButtonPA.setColorFilter(ContextCompat.getColor(requireContext(), R.color.purple))

        val playIntent = Intent(requireActivity(), MusicService::class.java)
        playIntent.putExtra("action", START_PLAYING)

        val episode = playlistViewModel.getEpisode()
        playIntent.putExtra("audio", episode.audio)
        playIntent.putExtra("title", episode.title)
        playIntent.putExtra("thumbnail", episode.thumbnail)
        requireContext().startService(playIntent)
    }

    fun togglePlayingUI() {
        binding.playPauseButton.setIconResource(if(isPlaying) R.drawable.play else R.drawable.pause)
        isPlaying = !isPlaying
    }

    fun newEpisodePlayUI(duration: Int) {
        buttonMask = false
        isPlaying = true

        binding.podcastLoadingSkeleton.showOriginal()
        binding.playPauseButton.setIconResource(R.drawable.pause)

        val startTime = "00:00"
        binding.tvSeekbarStart.text = startTime
        binding.tvSeekbarEnd.text = formatDuration(duration)
        binding.seekBarPA.progress = 0
        binding.seekBarPA.max = duration
    }

    fun updateTimeUI(position: Int) {
        binding.tvSeekbarStart.text = formatDuration(position)
        binding.seekBarPA.progress = position
    }

    fun playNextEpisode() {
        playlistViewModel.toNextEpisode()
        playNewEpisode()
    }

    fun playPreviousEpisode() {
        playlistViewModel.toPreviousEpisode()
        playNewEpisode()
    }

    private fun formatDuration(duration: Int): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(duration.toLong())
        val seconds = TimeUnit.MILLISECONDS.toSeconds(duration.toLong()) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

}