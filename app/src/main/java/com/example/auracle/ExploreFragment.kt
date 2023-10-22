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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExploreFragment : Fragment() {

    private lateinit var binding: FragmentExploreBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentExploreBinding.inflate(layoutInflater)
        populateGenreCards()

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank())
                    onSearch(query)
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

        rcvGenreList.layoutManager = GridLayoutManager(context, 2)
        rcvGenreList.adapter = GenreCardAdapter(Data.highLevelGenreList)
        rcvGenreList.setHasFixedSize(true)
//        Log.wtf("Explore", Data.highLevelGenreList.toString())
    }

    private fun onSearch(query: String) {

        lifecycleScope.launch(Dispatchers.IO) {
            val podcastList = ListenNoteApi().search(query)
            if (!podcastList.isNullOrEmpty()) {
                withContext(Dispatchers.Main) {

//                    Log.d("ExploreFragment", "Podcast List: $podcastList")
                    binding.rcvGenreList.visibility = View.GONE

                    binding.rcvPodcastList.visibility = View.VISIBLE
                    binding.rcvPodcastList.layoutManager = LinearLayoutManager(context)
                    binding.rcvPodcastList.adapter = SearchPodcastCardAdapter(podcastList)
                }
            }
        }

    }

}