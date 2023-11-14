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
import androidx.room.Room
import com.example.auracle.com.example.auracle.PlayerInterface
import com.example.auracle.com.example.auracle.api.roomapi.AppDatabase
import com.example.auracle.com.example.auracle.viewmodel.PlaylistViewModel
import com.example.auracle.databinding.FragmentPlayerBinding
import com.example.auracle.datapack.listennote.ListenEpisodeShort
import com.example.auracle.service.MusicService
import com.example.auracle.service.NotificationReceiver
import com.squareup.picasso.Picasso
import java.io.File
import java.util.concurrent.TimeUnit

class PlayerFragment : Fragment(), PlayerInterface {

    companion object {
        const val tag = "PlayerFragment"
        var isFavorite: Boolean = false
        var fIndex:Int = -1
    }

    private var musicService: MusicService? = null
    private lateinit var binding: FragmentPlayerBinding
    private val playlistViewModel: PlaylistViewModel by activityViewModels()

    private var isPlaying = false
    private var isLooping = false
    private var source: String? = null
    private var buttonMask = true  // buttonMask should mask the actions of player buttons

    private val notificationReceiver = NotificationReceiver()
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

            requireActivity().bindService(Intent(requireContext(), MusicService::class.java), serviceConnection,
                AppCompatActivity.BIND_AUTO_CREATE
            )

            val episodeList = it.getParcelableArrayList<ListenEpisodeShort>("episodeList")
            val index = it.getInt("index")
            source = it.getString("class")

            try {
//                if (source == "PodcastDetailsFragment")
                playlistViewModel.initPlaylist(episodeList!!, index)
//                else ""

            } catch (e: Exception) {
                Log.d("PlayerFragment", "Error: $e")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayerBinding.inflate(layoutInflater)

        registerInteractive()
        registerBroadcast()
        playlistViewModel.hideNowPlaying()

        if (source == "PodcastDetailsFragment" || source == "FavoriteFragment")
            playNewEpisode()
        else if (source == "NowPlayingFragment"){
            val intent = Intent(requireActivity(), MusicService::class.java)
            intent.putExtra("action", PlayerInterface.RESUME_PLAYING)
            requireContext().startService(intent)
        }

        return binding.root
    }

    private fun registerInteractive() {
        binding.playPauseButton.setOnClickListener {
            if (!buttonMask) {
                val togglePlayIntent = Intent(requireActivity(), MusicService::class.java)
                togglePlayIntent.putExtra("action", PlayerInterface.TOGGLE_PLAYING)
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
            intent.putExtra("action", PlayerInterface.TOGGLE_LOOPING)
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

        binding.favoriteBTNPA.setOnClickListener {
           if(!buttonMask){
               if(isFavorite){
                   isFavorite = false
                   binding.favoriteBTNPA.setImageResource(R.drawable.favorite_empty)
                   FavoriteFragment.favoritePodcasts.removeAt(fIndex)
               }
               else{
                   isFavorite = true
                   binding.favoriteBTNPA.setImageResource(R.drawable.favorite)
                   FavoriteFragment.favoritePodcasts.add(playlistViewModel.getEpisode())
               }
           }
        }

        binding.seekBarPA.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val intent = Intent(requireActivity(), MusicService::class.java)
                    intent.putExtra("action", PlayerInterface.SEEK)
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
        intentFilter.addAction(PlayerInterface.START_PLAYING)
//        intentFilter.addAction(STOP_PLAYING)
        intentFilter.addAction(PlayerInterface.PLAY_NEXT)
        intentFilter.addAction(PlayerInterface.PLAY_PREVIOUS)
        requireContext().registerReceiver(notificationReceiver, intentFilter, AppCompatActivity.RECEIVER_EXPORTED)
    }

    private fun playNewEpisode() {

        buttonMask = true
        isPlaying = false

        binding.podcastLoadingSkeleton.showSkeleton()
        binding.playPauseButton.setIconResource(R.drawable.play)

        if(isLooping) binding.repeatButtonPA.setColorFilter(ContextCompat.getColor(requireContext(), R.color.purple))

        val playIntent = Intent(requireActivity(), MusicService::class.java)
        playIntent.putExtra("action", PlayerInterface.START_PLAYING)

        val episode = playlistViewModel.getEpisode()
        if (episode != null) {
            val playIntent = Intent(requireActivity(), MusicService::class.java)
            playIntent.putExtra("action", PlayerInterface.START_PLAYING)
            playIntent.putExtra("audio", episode.audio)
            playIntent.putExtra("title", episode.title)
            playIntent.putExtra("thumbnail", episode.thumbnail)
            requireContext().startService(playIntent)

            fIndex = favoriteChecker(playlistViewModel.getEpisode().id)
            if (isFavorite) binding.favoriteBTNPA.setImageResource(R.drawable.favorite)
            else binding.favoriteBTNPA.setImageResource(R.drawable.favorite_empty)
        }

    }

    private fun returnAudioSource(episode: ListenEpisodeShort): String {
        return if (episode.offlineLocation.isNullOrBlank()) episode.audio!!
        else episode.offlineLocation!!
    }

    override fun togglePlayingUI() {
        binding.playPauseButton.setIconResource(if(isPlaying) R.drawable.play else R.drawable.pause)
        isPlaying = !isPlaying
    }

    override fun newEpisodePlayUI(progress:Int, duration: Int) {
        fIndex = favoriteChecker(playlistViewModel.getEpisode().id)

        buttonMask = false
        isPlaying = true


        Picasso.get().load(playlistViewModel.getEpisode().thumbnail).into(binding.PodcastThumbnail)
        binding.EpisodeNamePA.text = playlistViewModel.getEpisode().title

        binding.podcastLoadingSkeleton.showOriginal()
        binding.playPauseButton.setIconResource(R.drawable.pause)


        binding.tvSeekbarStart.text = formatDuration(progress)
        binding.tvSeekbarEnd.text = formatDuration(duration)
        binding.seekBarPA.progress = progress
        binding.seekBarPA.max = duration


        if(isFavorite) binding.favoriteBTNPA.setImageResource(R.drawable.favorite)
        else binding.favoriteBTNPA.setImageResource((R.drawable.favorite_empty))
    }

    override fun updateTimeUI(position: Int) {
        binding.tvSeekbarStart.text = formatDuration(position)
        binding.seekBarPA.progress = position
    }

    override fun playNextEpisode() {
        playlistViewModel.toNextEpisode()
        playNewEpisode()
    }

    override fun playPreviousEpisode() {
        playlistViewModel.toPreviousEpisode()
        playNewEpisode()
    }

    private fun showNowPlaying() {
        playlistViewModel.showNowPlaying()
    }

    private fun formatDuration(duration: Int): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(duration.toLong())
        val seconds = TimeUnit.MILLISECONDS.toSeconds(duration.toLong()) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        showNowPlaying()
    }

    fun favoriteChecker(id:String?):Int{
        isFavorite= false
        FavoriteFragment.favoritePodcasts.forEachIndexed { index, listenEpisodeShort ->
            if(id == listenEpisodeShort.id){
                isFavorite= true
                return index
            }
        }
        return -1
    }



}