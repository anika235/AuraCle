package com.example.auracle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.auracle.api.ListenNoteApi
import com.example.auracle.com.example.auracle.rcvpopulargenre.PopularGenreAdapter
import com.example.auracle.com.example.auracle.rcvpopulargenre.PopularGenreViewHolder
import com.example.auracle.com.example.auracle.rcvpopularpodcast.PopularPodcastAdapter
import com.example.auracle.databinding.FragmentHomeBinding
import com.example.auracle.datapack.listennote.ListenGenre
import com.example.auracle.datapack.listennote.ListenSearchPodcast
import com.example.auracle.dataholder.FixedData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

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
        var genreList = FixedData.highLevelGenreList
        genreList = (arrayListOf(ListenGenre(null, "All", null, null)) + genreList) as ArrayList<ListenGenre>

        binding.rcvPopularList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rcvPopularList.adapter = PopularGenreAdapter(
            genreList,
            this::populatePodcastPopularList
        )
    }

    private fun populatePodcastPopularList(id: Int?, viewHolder: PopularGenreViewHolder) {
        viewHolder.popularGenreSkeleton.showSkeleton()

        viewHolder.popularPodcastGhost.visibility = View.VISIBLE
        viewHolder.rcvPopularList.visibility = View.GONE


        lifecycleScope.launch(Dispatchers.IO) {

            val bestPodcastList = listenNoteApi.bestSearch(id?.toString())

            withContext(Dispatchers.Main) {
                viewHolder.rcvPopularList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                viewHolder.rcvPopularList.adapter = PopularPodcastAdapter(bestPodcastList) {
                    onItemClicked(it)
                }

                viewHolder.popularPodcastGhost.visibility = View.GONE
                viewHolder.rcvPopularList.visibility = View.VISIBLE

                viewHolder.popularGenreSkeleton.showOriginal()
            }

        }
    }

    private fun onItemClicked(podcast: ListenSearchPodcast) {
        val fragment = PodcastDetailsFragment()
        val args = Bundle()
        args.putString("podcastId", podcast.id)
        fragment.arguments = args

        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.fragmentDisplay, fragment)
        fragmentTransaction.commit()
    }

}