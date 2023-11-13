package com.example.auracle

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.auracle.databinding.FavoriteviewBinding
import com.example.auracle.datapack.listennote.ListenEpisodeShort
import com.squareup.picasso.Picasso

class favorite_adapter(private val episodeList: ArrayList<ListenEpisodeShort>,private val onCardClicked: ((ArrayList<ListenEpisodeShort>,Int) -> Unit) ): RecyclerView.Adapter<favorite_adapter.MyHolder>(){
    class MyHolder(val binding: FavoriteviewBinding): RecyclerView.ViewHolder(binding.root){
        val image = binding.podcastImageFV
        val name = binding.podcastnameFV
        val root = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(FavoriteviewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.name.text = episodeList[position].title
        Picasso.get().load(episodeList[position].thumbnail).into(holder.image)

        holder.root.setOnClickListener {
            onCardClicked(episodeList,position)
        }

    }

    override fun getItemCount(): Int {
        return episodeList.size
    }

}