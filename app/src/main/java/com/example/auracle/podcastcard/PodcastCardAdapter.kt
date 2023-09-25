package com.example.auracle.podcastcard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.auracle.R

class PodcastCardAdapter(private val favoritePodcastList: ArrayList<PodcastCard>):
    RecyclerView.Adapter<PodcastCardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PodcastCardViewHolder {
        return PodcastCardViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.podcast_card, parent, false))
    }

    override fun getItemCount(): Int {
        return favoritePodcastList.size
    }

    override fun onBindViewHolder(holder: PodcastCardViewHolder, position: Int) {
        val currentItem = favoritePodcastList[position]
        holder.podcastImage.setImageResource(currentItem.podcastImage)
    }
}