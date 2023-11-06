package com.example.auracle.com.example.auracle.rcvpopularpodcast

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.auracle.R

class PopularPodcastViewHolder(itemView: View) : ViewHolder(itemView) {
    val imgPodcastThumbnail: ImageView = itemView.findViewById(R.id.imgPodcastThumbnail)
    val txtPodcastTitle: TextView = itemView.findViewById(R.id.txtPodcastTitle)
    val txtPodcastAuthor: TextView = itemView.findViewById(R.id.txtPodcastAuthor)

}