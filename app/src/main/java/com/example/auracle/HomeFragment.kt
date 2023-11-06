package com.example.auracle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.auracle.api.ListenNoteApi
import com.example.auracle.com.example.auracle.rcvpopulargenre.PopularGenreAdapter
import com.example.auracle.com.example.auracle.rcvpopularpodcast.PopularPodcastAdapter
import com.example.auracle.databinding.FragmentHomeBinding
import com.example.auracle.datapack.listennote.ListenGenre
import com.example.auracle.fixeddata.Data
import com.faltenreich.skeletonlayout.Skeleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private val TAG = "HomeFragment"

    private lateinit var binding: FragmentHomeBinding
    private val listenNoteApi = ListenNoteApi()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate((layoutInflater))

        populateGenrePopularList()

        return binding.root
    }

    private fun populateGenrePopularList() {
        val genreList = Data.highLevelGenreList
        genreList.add(0, ListenGenre(null, "All", null, null))
        binding.rcvPopularList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rcvPopularList.adapter = PopularGenreAdapter(genreList) { id, rvcPopularPodcast, popularGenreSkeleton ->
            populatePodcastPopularList(id, rvcPopularPodcast, popularGenreSkeleton)
        }
    }

    private fun populatePodcastPopularList(id: Int?, rvcPopularPodcast: RecyclerView, popularGenreSkeleton: Skeleton) {
        popularGenreSkeleton.showSkeleton()
        rvcPopularPodcast.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        lifecycleScope.launch(Dispatchers.IO) {

            val bestPodcastList = listenNoteApi.bestSearch(id?.toString())

            withContext(Dispatchers.Main) {
                rvcPopularPodcast.adapter = PopularPodcastAdapter(bestPodcastList)
                popularGenreSkeleton.showOriginal()
            }

        }

    }

}