package com.example.auracle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.auracle.com.example.auracle.viewmodel.HomeViewModel
import com.example.auracle.databinding.FragmentPodcastDetailsBinding
import com.example.auracle.datapack.listennote.ListenPodcastLong
import com.example.auracle.episodecard.EpisodeCardAdapter
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.squareup.picasso.Picasso

class PodcastDetailsFragment : Fragment() {

    private lateinit var binding: FragmentPodcastDetailsBinding
    private lateinit var podcastId: String
    private val playlistViewModel: HomeViewModel by activityViewModels()

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

        binding.rcvPodcastEpisodeList.addItemDecoration(MaterialDividerItemDecoration(requireContext(),LinearLayoutManager.VERTICAL))
        binding.rcvPodcastEpisodeList.layoutManager = LinearLayoutManager(requireContext())

        playlistViewModel.playlist.observe(viewLifecycleOwner, this::initializeLayout)

        getPodcastDetails()

        return binding.root
    }

    private fun initializeLayout(podcastDetails: ListenPodcastLong) {

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

        binding.rcvPodcastEpisodeList.adapter =
            EpisodeCardAdapter(requireContext(), podcastDetails.episodes)

        binding.podcastDetailSkeleton.showOriginal()
    }

    private fun getPodcastDetails() {
        binding.podcastDetailSkeleton.showSkeleton()
        playlistViewModel.retrievePodcast(podcastId)
    }
}