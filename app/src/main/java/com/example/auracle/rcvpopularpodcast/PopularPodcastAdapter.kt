package com.example.auracle.com.example.auracle.rcvpopularpodcast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.auracle.R
import com.example.auracle.datapack.listennote.ListenSearchPodcast
import com.squareup.picasso.Picasso

class PopularPodcastAdapter(
    private val podcastList: ArrayList<ListenSearchPodcast>,
    private val onItemClicked: ((ListenSearchPodcast) -> Unit)
) : RecyclerView.Adapter<PopularPodcastViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularPodcastViewHolder {
        return PopularPodcastViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.popular_podcast_card, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return podcastList.size
    }

    override fun onBindViewHolder(holder: PopularPodcastViewHolder, position: Int) {
        val currentItem = podcastList[position]

        holder.txtPodcastTitle.text = currentItem.titleOriginal
        holder.txtPodcastAuthor.text = currentItem.publisherOriginal
        Picasso.get().load(currentItem.thumbnail).into(holder.imgPodcastThumbnail)

        holder.llPopularPodcast.setOnClickListener { onItemClicked(currentItem) }

    }
}