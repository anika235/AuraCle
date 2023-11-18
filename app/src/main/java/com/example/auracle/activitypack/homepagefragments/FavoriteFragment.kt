package com.example.auracle.com.example.auracle.activitypack.homepagefragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.auracle.com.example.auracle.rcvpack.FavoriteAdapter
import com.example.auracle.R
import com.example.auracle.com.example.auracle.api.firebase.FirebaseRealtime
import com.example.auracle.databinding.FragmentFavoriteBinding
import com.example.auracle.datapack.listennote.ListenEpisodeShort
import com.faltenreich.skeletonlayout.applySkeleton

class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private val firebaseRealtime = FirebaseRealtime.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentFavoriteBinding.inflate((layoutInflater))

        binding.favoriteRV.setHasFixedSize(true)
        binding.favoriteRV.setItemViewCacheSize(13)
        binding.favoriteRV.layoutManager = GridLayoutManager(requireContext(), 2)

        val favoriteRVSkeleton = binding.favoriteRV.applySkeleton(R.layout.favoriteview)
        favoriteRVSkeleton.showSkeleton()

        firebaseRealtime.getFavorites()?.addOnCompleteListener{
            if(it.isSuccessful){
                val values = it.result!!.value as HashMap<*, *>

                val favoriteList: ArrayList<ListenEpisodeShort> = ArrayList()

                values.forEach { (_, va) ->
                    val episode = ListenEpisodeShort(va as Map<*, *>)
                    favoriteList.add(episode)
                }
                favoriteRVSkeleton.showOriginal()
                binding.favoriteRV.adapter = FavoriteAdapter(favoriteList, this::toPlayer)
            }
        }

        return binding.root

    }
    private fun toPlayer(episode: ListenEpisodeShort) {

        val playerFragment = PlayerFragment()
        val bundle = Bundle()
        bundle.putParcelableArrayList("episodeList", arrayListOf(episode))
        bundle.putInt("index", 0)
        bundle.putString("class", "FavoriteFragment")
        playerFragment.arguments = bundle

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentDisplay, playerFragment, PlayerFragment.tag)
            .addToBackStack(null)
            .commit()
    }

}