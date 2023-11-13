package com.example.auracle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.auracle.databinding.FragmentFavoriteBinding
import com.example.auracle.datapack.listennote.ListenEpisodeShort

class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var adapter: favorite_adapter
    companion object{
        var favoritePodcasts: ArrayList<ListenEpisodeShort> = ArrayList()

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFavoriteBinding.inflate((layoutInflater))

        binding.favoriteRV.setHasFixedSize(true)
        binding.favoriteRV.setItemViewCacheSize(13)
        binding.favoriteRV.layoutManager = LinearLayoutManager(requireContext())
        adapter = favorite_adapter(FavoriteFragment.favoritePodcasts, this::toPlayer)
        binding.favoriteRV.adapter = adapter
        return binding.root

    }
    private fun toPlayer(podcastEpisodes: ArrayList<ListenEpisodeShort>, position: Int) {

        val playerFragment = PlayerFragment()
        val bundle = Bundle()
        bundle.putParcelableArrayList("episodeList", podcastEpisodes)
        bundle.putInt("index", position)
        bundle.putString("class", "FavoriteFragment")
        playerFragment.arguments = bundle

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentDisplay, playerFragment, PlayerFragment.tag)
            .addToBackStack(null)
            .commit()
    }

}