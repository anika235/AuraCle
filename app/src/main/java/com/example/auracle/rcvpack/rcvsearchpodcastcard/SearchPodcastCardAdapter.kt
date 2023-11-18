package com.example.auracle.com.example.auracle.rcvpack.rcvsearchpodcastcard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.parseAsHtml
import androidx.recyclerview.widget.RecyclerView
import com.example.auracle.R
import com.example.auracle.datapack.listennote.ListenSearchPodcast
import com.squareup.picasso.Picasso


class SearchPodcastCardAdapter(private val searchPodcastList: ArrayList<ListenSearchPodcast>, private val onItemClicked: ((ListenSearchPodcast) -> Unit)): RecyclerView.Adapter<SearchPodcastCardViewHolder>() {
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
        holder.podcastClickable.setOnClickListener {
//            itemClickListener(currentItem.id!!)

            onItemClicked(currentItem)
//            it.context.startActivity(Intent(it.context, PodcastDetails::class.java).apply {
//                putExtra("podcast_id", currentItem.id)
//            })

        }
    }
}