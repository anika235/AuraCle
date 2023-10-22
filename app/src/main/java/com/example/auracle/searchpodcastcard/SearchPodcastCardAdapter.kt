package com.example.auracle.searchpodcastcard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.parseAsHtml
import androidx.recyclerview.widget.RecyclerView
import com.example.auracle.R
import com.example.auracle.datapack.listennote.ListenPodcastShort
import com.squareup.picasso.Picasso

class SearchPodcastCardAdapter(private val searchPodcastList: ArrayList<ListenPodcastShort>): RecyclerView.Adapter<SearchPodcastCardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchPodcastCardViewHolder {
        return SearchPodcastCardViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.search_podcast_card, parent, false))
    }

    override fun getItemCount(): Int {
        return searchPodcastList.size
    }

    override fun onBindViewHolder(holder: SearchPodcastCardViewHolder, position: Int) {
        val currentItem = searchPodcastList[position]

        Picasso.get().load(currentItem.thumbnail).into(holder.podcastImage)
        holder.podcastTitle.text = currentItem.titleOriginal
        holder.podcastAuthor.text = currentItem.publisherOriginal
        holder.podcastDescription.text = currentItem.descriptionHighlighted?.parseAsHtml()
    }
}