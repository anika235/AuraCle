package com.example.auracle.com.example.auracle.rcvpack.rcvpopulargenre

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.auracle.R
import com.example.auracle.datapack.listennote.ListenGenre

class PopularGenreAdapter(
    private val genreList: ArrayList<ListenGenre>,
    private val loadPodcasts: ((Int?, PopularGenreViewHolder) -> Unit)
) :
    RecyclerView.Adapter<PopularGenreViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularGenreViewHolder {
        return PopularGenreViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.popular_genre, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return genreList.size
    }

    override fun onBindViewHolder(holder: PopularGenreViewHolder, position: Int) {
        val currentItem = genreList[position]

//        holder.popularGenreSkeleton.showSkeleton()

        var titleText = "Popular"
        titleText += if (currentItem.id != null) " in ${currentItem.name}" else " & Trending"
        holder.popularTitle.text = titleText

        loadPodcasts(currentItem.id, holder)
    }
}