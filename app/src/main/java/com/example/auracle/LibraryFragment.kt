package com.example.auracle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.auracle.databinding.FragmentLibraryBinding
import com.example.auracle.podcastcard.PodcastCard
import com.example.auracle.podcastcard.PodcastCardAdapter

class LibraryFragment : Fragment() {

    private lateinit var binding: FragmentLibraryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLibraryBinding.inflate(layoutInflater)

        val podcastList = arrayListOf<PodcastCard>()
        val cardId = arrayOf(
            R.drawable.p1,
            R.drawable.p2,
            R.drawable.p3,
            R.drawable.p4,
            R.drawable.p5,
        )

        for (id in cardId) podcastList.add(PodcastCard(id))

        val rcvPodcasts = binding.rcvPodcastList

        rcvPodcasts.layoutManager = GridLayoutManager(context, 3)
        rcvPodcasts.adapter = PodcastCardAdapter(podcastList)
        rcvPodcasts.setHasFixedSize(true)

        return binding.root
    }

}