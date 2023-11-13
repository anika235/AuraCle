package com.example.auracle

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.example.auracle.com.example.auracle.PlayerInterface
import com.example.auracle.com.example.auracle.viewmodel.PlaylistViewModel
import com.example.auracle.databinding.FragmentNowPlayingBinding
import com.example.auracle.service.MusicService
import com.squareup.picasso.Picasso

class NowPlayingFragment : Fragment(), PlayerInterface {

    lateinit var binding: FragmentNowPlayingBinding
    private var musicService: MusicService? = null
    private val playlistViewModel: PlaylistViewModel by activityViewModels()
    private var isPlaying = true
    private var buttonMask = false
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as MusicService.LocalBinder
            musicService = binder.getService()
            musicService!!.setParent(this@NowPlayingFragment)
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            return
        }
    }

    override fun onStart() {
        super.onStart()
        requireActivity().bindService(
            Intent(requireContext(), MusicService::class.java), serviceConnection,
            AppCompatActivity.BIND_AUTO_CREATE
        )


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentNowPlayingBinding.inflate(layoutInflater)

        if (playlistViewModel.playList.isNotEmpty()) {
            binding.podcastName.text = playlistViewModel.getEpisode().title
            Picasso.get().load(playlistViewModel.getEpisode().thumbnail).into(binding.podcastImgNp)
        }
        registerInteractive()

        return binding.root
    }

    private fun registerInteractive() {

        binding.llClickable.setOnClickListener {

            var playerFragment = requireActivity().supportFragmentManager.findFragmentByTag(PlayerFragment.tag)

            if (playerFragment == null) {
                playerFragment = PlayerFragment()

                val bundle = Bundle()
                bundle.putParcelableArrayList("episodeList", playlistViewModel.playList)
                bundle.putInt("index", playlistViewModel.playlistPosition)
                bundle.putString("class", "NowPlayingFragment")
                playerFragment.arguments = bundle
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentDisplay, playerFragment, "PlayerFragment")
                    .addToBackStack(null)
                    .commit()
            } else {
                requireActivity().supportFragmentManager.popBackStack(PlayerFragment.tag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }
        }

        binding.PlayPauseBtnNp.setOnClickListener {
            if (!buttonMask) {
                val togglePlayIntent = Intent(requireActivity(), MusicService::class.java)
                togglePlayIntent.putExtra("action", PlayerInterface.TOGGLE_PLAYING)
                requireContext().startService(togglePlayIntent)
            }
        }

        binding.nextBtnNP.setOnClickListener {
            if (!buttonMask)
                playNextEpisode()
        }

    }

    private fun playNewEpisode() {

        buttonMask = true
        isPlaying = false

        binding.PlayPauseBtnNp.setIconResource(R.drawable.play)
        binding.podcastName.text = playlistViewModel.getEpisode().title
        binding.progressBarNp.visibility = View.VISIBLE

        val playIntent = Intent(requireActivity(), MusicService::class.java)
        playIntent.putExtra("action", PlayerInterface.START_PLAYING)

        val episode = playlistViewModel.getEpisode()
        playIntent.putExtra("audio", episode.audio)
        playIntent.putExtra("title", episode.title)
        playIntent.putExtra("thumbnail", episode.thumbnail)
        requireContext().startService(playIntent)
    }

    override fun updateTimeUI(position: Int) {
        return
    }

    override fun newEpisodePlayUI(progress: Int, duration: Int) {
        buttonMask = false
        isPlaying = true

        binding.progressBarNp.visibility = View.GONE
    }

    override fun togglePlayingUI() {
        binding.PlayPauseBtnNp.setIconResource(if (isPlaying) R.drawable.play else R.drawable.pause)
        isPlaying = !isPlaying
    }

    override fun playNextEpisode() {
        playlistViewModel.toNextEpisode()
        playNewEpisode()
    }

    override fun playPreviousEpisode() {
        TODO("Not yet implemented")
    }


}