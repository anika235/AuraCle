package com.example.auracle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.auracle.databinding.FragmentNowPlayingBinding
import com.squareup.picasso.Picasso

class NowPlaying : Fragment() {
//    companion object{
//        lateinit var Playerbindings: ActivityPlayerBinding
//        lateinit var binding: FragmentNowPlayingBinding
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
//    ): View {
//        binding = FragmentNowPlayingBinding.inflate(inflater, container, false)
//
//        binding.root.visibility = View.INVISIBLE
//        binding.PlayPauseBtnNp.setOnClickListener {
//            if (Player.isPlaying) pauseMusic()
//            else playMusic()
//        }
//
//        return binding.root
//    }
//
//
//    override fun onResume() {
//        super.onResume()
//
//        binding.root.visibility = View.VISIBLE
//        Playerbindings.podcastLoadingSkeleton.showSkeleton()
//        Picasso.get().load(Player.podcastListPA[Player.podcastPosition].thumbnail).into(
//           binding.podcastImgNp)
//        binding.podcastnameNP.text = Player.podcastListPA[Player.podcastPosition].title
//        if(Player.isPlaying) binding.PlayPauseBtnNp.setIconResource(R.drawable.pause)
//        else binding.PlayPauseBtnNp.setIconResource(R.drawable.play)
//
//    }
//
//    private fun playMusic(){
//        Player.musicService.mediaPlayer.start()
//        binding.PlayPauseBtnNp.setIconResource(R.drawable.pause)
//        Player.musicService.showNotification(R.drawable.pause)
//        Playerbindings.nextBtn.setIconResource(R.drawable.pause)
//        Player.isPlaying = true
//    }
//
//    private fun pauseMusic(){
//        Player.musicService.mediaPlayer.pause()
//        binding.PlayPauseBtnNp.setIconResource(R.drawable.play)
//        Player.musicService.showNotification(R.drawable.play)
//        Player.binding.nextBtn.setIconResource(R.drawable.play)
//        Player.isPlaying = false
//    }

}