package com.example.auracle.podcastcard

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.auracle.R

class PodcastCardViewHolder(itemView: View): ViewHolder(itemView) {
    val podcastImage: ImageView = itemView.findViewById(R.id.imgPodcast)
}