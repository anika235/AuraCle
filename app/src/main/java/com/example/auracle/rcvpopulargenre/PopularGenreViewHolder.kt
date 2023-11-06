package com.example.auracle.com.example.auracle.rcvpopulargenre

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.auracle.R
import com.faltenreich.skeletonlayout.Skeleton

class PopularGenreViewHolder(itemView: View): ViewHolder(itemView) {
    val popularPodcastGhost: LinearLayout = itemView.findViewById(R.id.popularPodcastGhost)
    val popularGenreSkeleton: Skeleton = itemView.findViewById(R.id.popularGenreSkeleton)
    val popularTitle: TextView = itemView.findViewById(R.id.popularTitle)
    val rcvPopularList: RecyclerView = itemView.findViewById(R.id.rcvPopularPodcasts)
}