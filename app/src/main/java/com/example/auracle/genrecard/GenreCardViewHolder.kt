package com.example.auracle.genrecard

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.auracle.R

class GenreCardViewHolder(itemView: View): ViewHolder(itemView) {
    val genreName: TextView = itemView.findViewById(R.id.genreName)
    val genreImage: ImageView = itemView.findViewById(R.id.genreImg)
    val genreBg: CardView = itemView.findViewById(R.id.genreBg)
}