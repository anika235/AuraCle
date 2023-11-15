package com.example.auracle.com.example.auracle.rcvdownloaded

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.auracle.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView

class DownloadedViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val imgDownloaded: ShapeableImageView = itemView.findViewById(R.id.imgDownloaded)
    val txtPodcastName: TextView = itemView.findViewById(R.id.txtPodcastName)
    val btnPlay: MaterialButton = itemView.findViewById(R.id.btnPlay)
    val btnDownload: MaterialButton = itemView.findViewById(R.id.btnDownload)
}