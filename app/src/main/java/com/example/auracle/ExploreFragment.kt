package com.example.auracle

import android.os.Bundle
import android.util.Log

import androidx.fragment.app.Fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager

import com.example.auracle.databinding.FragmentExploreBinding
import com.example.auracle.fixeddata.Data
import com.example.auracle.genrecard.GenreCardAdapter

class ExploreFragment : Fragment() {

    private lateinit var binding: FragmentExploreBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentExploreBinding.inflate(layoutInflater)

//        val podcastList = arrayListOf<PodcastCard>()
//        val cardId = arrayOf(
//            R.drawable.p1,
//            R.drawable.p2,
//            R.drawable.p3,
//            R.drawable.p4,
//            R.drawable.p5,
//        )
//
//        for (id in cardId) podcastList.add(PodcastCard(id))
//
//        val rcvPodcasts = binding.rcvPodcastList
//
//        rcvPodcasts.layoutManager = GridLayoutManager(context, 3)
//        rcvPodcasts.adapter = PodcastCardAdapter(podcastList)
//        rcvPodcasts.setHasFixedSize(true)

        populateGenreCards()

        return binding.root
    }

    private fun populateGenreCards() {
        val rcvGenreList = binding.rcvGenreList

        rcvGenreList.layoutManager = GridLayoutManager(context, 2)
        rcvGenreList.adapter = GenreCardAdapter(Data.genreList)
        rcvGenreList.setHasFixedSize(true)
        Log.wtf("Explore", Data.genreList.toString())

    }

}