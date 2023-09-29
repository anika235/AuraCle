package com.example.auracle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.auracle.podcastcard.PodcastCard
import com.example.auracle.podcastcard.PodcastCardAdapter

class exploreFragment : Fragment() {
    private lateinit var rcvPodcasts: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_explore, container, false)

        val podcastList = arrayListOf<PodcastCard>()
        val cardId = arrayOf(
            R.drawable.p1,
            R.drawable.p2,
            R.drawable.p3,
            R.drawable.p4,
            R.drawable.p5,
        )

        for (id in cardId) podcastList.add(PodcastCard(id))

        rcvPodcasts = view.findViewById(R.id.rcvPodcastList)
        rcvPodcasts.layoutManager = GridLayoutManager(context, 3)
        rcvPodcasts.adapter = PodcastCardAdapter(podcastList)
        rcvPodcasts.setHasFixedSize(true)

        return view
    }

}