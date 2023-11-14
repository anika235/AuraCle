package com.example.auracle.com.example.auracle

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.auracle.databinding.SubscribeviewBinding
import com.example.auracle.datapack.listennote.ListenPodcastLong
import com.squareup.picasso.Picasso

class subscribe_adapter (private val podcastList: ArrayList<ListenPodcastLong>, private val onCardClicked: ((ArrayList<ListenPodcastLong>, Int) -> Unit) ): RecyclerView.Adapter<subscribe_adapter.MyHolder>(){
    class MyHolder(val binding: SubscribeviewBinding): RecyclerView.ViewHolder(binding.root){
        val image = binding.podcastImgsubs
        val name = binding.podcastnameSubs
        val author = binding.podcastAuthor
        val root = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(SubscribeviewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.name.text = podcastList[position].title
        Picasso.get().load(podcastList[position].thumbnail).into(holder.image)
        holder.author.text = podcastList[position].publisher

        holder.root.setOnClickListener {
            onCardClicked(podcastList, position)
        }
    }


    override fun getItemCount(): Int {
        return podcastList.size
    }

}