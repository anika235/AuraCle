package com.example.auracle.genrecard

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.auracle.R
import com.example.auracle.datapack.listennote.ListenGenre

class GenreCardAdapter(private val genreList: ArrayList<ListenGenre>): RecyclerView.Adapter<GenreCardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreCardViewHolder {
        return GenreCardViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.genre_card, parent, false))
    }

    override fun getItemCount(): Int {
        return genreList.size
    }

    override fun onBindViewHolder(holder: GenreCardViewHolder, position: Int) {
        val currentItem = genreList[position]
        holder.genreName.text = currentItem.name
        holder.genreImage.setImageResource(currentItem.imgId!!)
        holder.genreBg.setCardBackgroundColor(Color.parseColor(currentItem.bgColor))

    }
}