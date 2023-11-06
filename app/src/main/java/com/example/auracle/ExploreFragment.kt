package com.example.auracle

import android.os.Bundle

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
import com.example.auracle.datapack.listennote.ListenSearchPodcast
import com.example.auracle.fixeddata.Data
import com.example.auracle.rcvgenrecard.GenreCardAdapter
import com.example.auracle.rcvsearchpodcastcard.SearchPodcastCardAdapter
import com.faltenreich.skeletonlayout.Skeleton
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
    }

    private fun searchInitiateUIChanges(): Skeleton {
        binding.rcvGenreList.visibility = View.GONE
        binding.rcvPodcastList.visibility = View.VISIBLE
        val skeleton = binding.rcvPodcastList.applySkeleton(R.layout.search_podcast_card)
        skeleton.showSkeleton()
        return skeleton
    }

    private fun onResultSetAdapter(podcastList: ArrayList<ListenSearchPodcast>) {
        binding.rcvPodcastList.adapter = SearchPodcastCardAdapter(podcastList) {
            toPodcastDetails(it.id!!)
        }
    }

    private fun onSearch(query: String) {

        val skeleton = searchInitiateUIChanges()

        lifecycleScope.launch(Dispatchers.IO) {
            val podcastList = listenNoteApi.search(query)
            if (!podcastList.isNullOrEmpty()) {
                withContext(Dispatchers.Main) {
//                    Log.d("ExploreFragment", "Podcast List: $podcastList")
                    skeleton.showOriginal()
                    onResultSetAdapter(podcastList)
                }
            }
        }

    }

    private fun searchByGenre(genreId: String? = null) {

        val skeleton = searchInitiateUIChanges()

        lifecycleScope.launch(Dispatchers.IO) {
            val podcastList = listenNoteApi.bestSearch(genreId)
            if (podcastList.isNotEmpty()) {
                withContext(Dispatchers.Main) {
                    skeleton.showOriginal()
                    onResultSetAdapter(podcastList)
                }
            }
        }
    }


    private fun toPodcastDetails(podcastId: String) {

        val fragment = PodcastDetailsFragment()
        val args = Bundle()
        args.putString("podcastId", podcastId)
        fragment.arguments = args

        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.fragmentDisplay, fragment)
        fragmentTransaction.commit()
    }

}