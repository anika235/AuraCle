package com.example.auracle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.auracle.databinding.ActivityFavoritesBinding
import com.example.auracle.datapack.listennote.ListenEpisodeShort

class FavoritesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoritesBinding
    private lateinit var adapter: favorite_adapter

    companion object{
        var favoritePodcasts: ArrayList<ListenEpisodeShort> = ArrayList()

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate((layoutInflater))
        setContentView(binding.root)

        binding.favoriteRV.setHasFixedSize(true)
        binding.favoriteRV.setItemViewCacheSize(13)
        binding.favoriteRV.layoutManager = GridLayoutManager(this, 4)
        adapter = favorite_adapter(this, favoritePodcasts)
        binding.favoriteRV.adapter = adapter




    }
}