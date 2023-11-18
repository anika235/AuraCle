package com.example.auracle.com.example.auracle.rcvpack

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.auracle.databinding.FavoriteviewBinding
import com.example.auracle.datapack.listennote.ListenEpisodeShort
import com.squareup.picasso.Picasso

class FavoriteAdapter(
    private val episodeList: ArrayList<ListenEpisodeShort>,
    private val onPlay: ((ListenEpisodeShort) -> Unit)
) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    class FavoriteViewHolder(val binding: FavoriteviewBinding) : RecyclerView.ViewHolder(binding.root) {
        val image = binding.podcastImageFV
        val name = binding.podcastnameFV
        val btnPlay = binding.playfromFav
//        val root = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder(FavoriteviewBinding.inflate(LayoutInflater.from(parent.context), parent,false))
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {

        val currentItem = episodeList[position]

        holder.name.text = currentItem.title
        Picasso.get().load(currentItem.thumbnail).into(holder.image)

        holder.btnPlay.setOnClickListener {
            onPlay(currentItem)
        }

    }

    override fun getItemCount(): Int {
        return episodeList.size
    }

}