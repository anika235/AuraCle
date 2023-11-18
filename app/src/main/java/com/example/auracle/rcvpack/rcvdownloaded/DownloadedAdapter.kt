package com.example.auracle.com.example.auracle.rcvpack.rcvdownloaded

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.auracle.R
import com.example.auracle.com.example.auracle.datapack.room.RoomEpisode
import com.squareup.picasso.Picasso

class DownloadedAdapter(
    private val downloadedList: ArrayList<RoomEpisode>,
    private val onPlay: ((RoomEpisode) -> Unit),
    private val onDownload: ((Int) -> Unit)
): RecyclerView.Adapter<DownloadedViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadedViewHolder {
        return DownloadedViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.download_card, parent, false))
    }

    override fun getItemCount(): Int {
        return downloadedList.size
    }

    override fun onBindViewHolder(holder: DownloadedViewHolder, position: Int) {
        val currentItem = downloadedList[position]

        Log.w("TTTTTT", "Current Item: $currentItem")

        Picasso.get().load(currentItem.thumbnail).into(holder.imgDownloaded)
        holder.txtPodcastName.text = currentItem.title
        holder.btnPlay.setOnClickListener {
            onPlay(currentItem)
        }
        holder.btnDownload.setOnClickListener {
            onDownload(position)
        }
    }

}