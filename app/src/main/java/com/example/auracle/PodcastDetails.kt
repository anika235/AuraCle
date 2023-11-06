package com.example.auracle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.auracle.api.ListenNoteApi
import com.example.auracle.databinding.ActivityPodcastDetailsBinding
import com.example.auracle.datapack.listennote.ListenEpisodeShort
import com.example.auracle.episodecard.EpisodeCardAdapter
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PodcastDetails : AppCompatActivity() {

    private lateinit var binding: ActivityPodcastDetailsBinding
    private lateinit var podcastId: String
    companion object {
        lateinit var episodes: ArrayList<ListenEpisodeShort>
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPodcastDetailsBinding.inflate(layoutInflater)
        binding.rcvPodcastEpisodeList.addItemDecoration(MaterialDividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        binding.rcvPodcastEpisodeList.layoutManager = LinearLayoutManager(this)

        podcastId = intent.getStringExtra("podcast_id")!!

        binding.podcastDetailSkeleton.showSkeleton()

        getPodcastDetails()

        setContentView(binding.root)
    }

    private fun getPodcastDetails() {
        lifecycleScope.launch(Dispatchers.IO) {

            val podcastDetails = ListenNoteApi().podcastDetails(podcastId)
            withContext(Dispatchers.Main) {
                episodes = podcastDetails.episodes;
                val time = ""
                if (podcastDetails.audioLengthSec!! / 3600 > 0) {
                    time.plus("${podcastDetails.audioLengthSec!! / 3600}h ")
                }
                if (podcastDetails.audioLengthSec!! / 60 > 0) {
                    time.plus("${podcastDetails.audioLengthSec!! / 60}m ")
                }

                Picasso.get().load(podcastDetails.thumbnail).into(binding.imgPodcastThumbnail)
                binding.txtPodcastTitle.text = podcastDetails.title
                binding.txtPodcastAuthor.text = podcastDetails.publisher
                binding.txtPodcastTotalTime.text = time
                binding.txtPodcastDescription.text = podcastDetails.description
                binding.rcvPodcastEpisodeList.adapter = EpisodeCardAdapter(this@PodcastDetails, episodes)
                binding.podcastDetailSkeleton.showOriginal()


            }

        }
    }

}