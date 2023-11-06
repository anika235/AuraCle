package com.example.auracle.rcvsearchpodcastcard

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.auracle.R

class SearchPodcastCardViewHolder(itemView: View): ViewHolder(itemView) {
    val podcastImage: ImageView = itemView.findViewById(R.id.imgSearchPodcastImage)
    val podcastTitle: TextView = itemView.findViewById(R.id.txtSearchPodcastTitle)
    val podcastAuthor: TextView = itemView.findViewById(R.id.txtSearchPodcastAuthor)
    val podcastDescription: TextView = itemView.findViewById(R.id.txtSearchPodcastDescription)
    val podcastClickable: LinearLayoutCompat = itemView.findViewById(R.id.llSearchPodcastHeader)
}