package com.example.auracle

import android.os.Bundle
import android.util.Log

import androidx.fragment.app.Fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.auracle.api.ListenNoteApi

import com.example.auracle.databinding.FragmentExploreBinding
import com.example.auracle.fixeddata.Data
import com.example.auracle.genrecard.GenreCardAdapter
import com.example.auracle.searchpodcastcard.SearchPodcastCardAdapter
import com.faltenreich.skeletonlayout.applySkeleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExploreFragment : Fragment() {

    private lateinit var binding: FragmentExploreBinding
    private val listenNoteApi = ListenNoteApi()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentExploreBinding.inflate(layoutInflater)

        binding.rcvGenreList.layoutManager = GridLayoutManager(context, 2)
        binding.rcvPodcastList.layoutManager = LinearLayoutManager(context)

        populateGenreCards()

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank())
                    onSearch(query)
                else
                    populateGenreCards()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        return binding.root
    }

    private fun populateGenreCards() {
        val rcvGenreList = binding.rcvGenreList

        binding.rcvGenreList.visibility = View.VISIBLE
        binding.rcvPodcastList.visibility = View.GONE


        rcvGenreList.adapter = GenreCardAdapter(Data.highLevelGenreList) {
            searchByGenre(it.id.toString())
        }
        rcvGenreList.setHasFixedSize(true)
//        Log.wtf("Explore", Data.highLevelGenreList.toString())
    }

    private fun onSearch(query: String) {

        binding.rcvGenreList.visibility = View.GONE
        binding.rcvPodcastList.visibility = View.VISIBLE
        val skeleton = binding.rcvPodcastList.applySkeleton(R.layout.search_podcast_card)
        skeleton.showSkeleton()

        lifecycleScope.launch(Dispatchers.IO) {
            val podcastList = listenNoteApi.search(query)
            if (!podcastList.isNullOrEmpty()) {
                withContext(Dispatchers.Main) {
//                    Log.d("ExploreFragment", "Podcast List: $podcastList")
                    skeleton.showOriginal()
                    binding.rcvPodcastList.adapter = SearchPodcastCardAdapter(podcastList)
                }
            }
        }

    }

    private fun searchByGenre(genreId: String? = null) {
        binding.rcvGenreList.visibility = View.GONE
        binding.rcvPodcastList.visibility = View.VISIBLE
        val skeleton = binding.rcvPodcastList.applySkeleton(R.layout.search_podcast_card)
        skeleton.showSkeleton()

        lifecycleScope.launch(Dispatchers.IO) {
            val podcastList = listenNoteApi.bestSearch(genreId)
            if (podcastList.isNotEmpty()) {
                withContext(Dispatchers.Main) {
                    skeleton.showOriginal()
                    binding.rcvPodcastList.adapter = SearchPodcastCardAdapter(podcastList)
                }
            }
        }
    }



}