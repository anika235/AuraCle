package com.example.auracle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.auracle.api.ListenNoteApi
import com.example.auracle.databinding.FragmentPodcastDetailsBinding
import com.example.auracle.datapack.listennote.ListenEpisodeShort
import com.example.auracle.episodecard.EpisodeCardAdapter
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PodcastDetailsFragment : Fragment() {

    private lateinit var binding: FragmentPodcastDetailsBinding
    private lateinit var podcastId: String

    companion object {
        lateinit var episodes: ArrayList<ListenEpisodeShort>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            podcastId = it.getString("podcastId").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPodcastDetailsBinding.inflate(layoutInflater)
        binding.rcvPodcastEpisodeList.addItemDecoration(
            MaterialDividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )
        binding.rcvPodcastEpisodeList.layoutManager = LinearLayoutManager(requireContext())

//        podcastId = intent.getStringExtra("podcast_id")!!

        binding.podcastDetailSkeleton.showSkeleton()

        getPodcastDetails()

        return binding.root
    }

    private fun getPodcastDetails() {
        lifecycleScope.launch(Dispatchers.IO) {

            val podcastDetails = ListenNoteApi().podcastDetails(podcastId)
            withContext(Dispatchers.Main) {

                episodes = podcastDetails.episodes

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

                binding.rcvPodcastEpisodeList.adapter = EpisodeCardAdapter(requireContext(), podcastDetails.episodes)

                binding.podcastDetailSkeleton.showOriginal()
            }

        }
    }
}