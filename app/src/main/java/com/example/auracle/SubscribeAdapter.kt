package com.example.auracle.com.example.auracle

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.auracle.databinding.SubscribeviewBinding
import com.example.auracle.datapack.listennote.ListenPodcastLong
import com.example.auracle.datapack.listennote.ListenSearchPodcast
import com.squareup.picasso.Picasso

class SubscribeAdapter(
    private val podcastList: ArrayList<ListenSearchPodcast>,
    private val onCardClicked: ((ListenSearchPodcast) -> Unit)
) : RecyclerView.Adapter<SubscribeAdapter.SubscribeViewHolder>() {

    class SubscribeViewHolder(val binding: SubscribeviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val image = binding.podcastImgsubs
        val name = binding.podcastnameSubs
        val author = binding.podcastAuthor
        val root = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscribeViewHolder {
        return SubscribeViewHolder(
            SubscribeviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SubscribeViewHolder, position: Int) {

        val currentItem = podcastList[position]

        Log.w("SubscribeAdapter", "currentItem: $currentItem")

        holder.name.text = currentItem.titleOriginal
        Picasso.get().load(currentItem.thumbnail).into(holder.image)
        holder.author.text = currentItem.publisherOriginal

        holder.root.setOnClickListener {
            onCardClicked(currentItem)
        }
    }


    override fun getItemCount(): Int {
        return podcastList.size
    }

}