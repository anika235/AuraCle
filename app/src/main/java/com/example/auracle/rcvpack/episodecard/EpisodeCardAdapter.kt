package com.example.auracle.com.example.auracle.rcvpack.episodecard

import android.view.LayoutInflater
import androidx.core.text.parseAsHtml
import androidx.recyclerview.widget.RecyclerView
import com.example.auracle.R
import com.example.auracle.datapack.listennote.ListenEpisodeShort
import java.text.SimpleDateFormat
import java.util.Date

class EpisodeCardAdapter(
    private val episodeList: ArrayList<ListenEpisodeShort>,
    private val onPlayListener: ((ArrayList<ListenEpisodeShort>, Int) -> Unit),
    private val onDownload: ((Int) -> Unit)
) : RecyclerView.Adapter<EpisodeCardViewHolder>() {
    override fun onCreateViewHolder(
        parent: android.view.ViewGroup,
        viewType: Int
    ): EpisodeCardViewHolder {
        return EpisodeCardViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.episode_card, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return episodeList.size
    }

    override fun onBindViewHolder(holder: EpisodeCardViewHolder, position: Int) {
        val currentItem = episodeList[position]
        val audioLen = "${currentItem.audioLengthSec!! / 60}m"
        holder.episodeTitle.text = currentItem.title
        holder.episodeDescription.text = currentItem.description?.parseAsHtml()
        holder.episodeDate.text =
            SimpleDateFormat("yyyy-MM-dd").format(Date(currentItem.pubDateMs!!))
        holder.episodePlay.text = audioLen
        holder.episodeDownload.setIconResource(if (currentItem.offlineLocation.isNullOrBlank()) R.drawable.baseline_arrow_circle_down_24 else R.drawable.baseline_download)

        if (currentItem.isDownloading) {
            holder.episodeDownload.visibility = android.view.View.GONE
            holder.progressIndicator.visibility = android.view.View.VISIBLE
        } else {
            holder.progressIndicator.visibility = android.view.View.GONE
            holder.episodeDownload.visibility = android.view.View.VISIBLE
        }

        holder.episodePlay.setOnClickListener {
            onPlayListener(episodeList, position)
        }
//        holder.episodeSubscribe.setOnClickListener {
//
//        }

        holder.episodeDownload.setOnClickListener {
            currentItem.isDownloading = true
            holder.episodeDownload.visibility = android.view.View.GONE
            holder.progressIndicator.visibility = android.view.View.VISIBLE

            onDownload(position)
        }

        ellipsizeDescription(holder, position)
    }

    private fun ellipsizeDescription(holder: EpisodeCardViewHolder, position: Int) {
        val wordCount = episodeList[position].description?.count { it == ' ' }
        val wordLimit = 30

        if (wordCount!! > wordLimit) {
            holder.episodeDescription.maxLines = 5
            holder.episodeDescription.ellipsize = android.text.TextUtils.TruncateAt.END
            holder.episodeDescriptionExpand.visibility = android.view.View.VISIBLE

            holder.episodeDescriptionExpand.setOnClickListener {
                holder.episodeDescription.maxLines = Int.MAX_VALUE
                holder.episodeDescription.ellipsize = null
                holder.episodeDescriptionExpand.visibility = android.view.View.GONE
                holder.episodeDescriptionCollapse.visibility = android.view.View.VISIBLE
            }

            holder.episodeDescriptionCollapse.setOnClickListener {
                holder.episodeDescription.maxLines = 5
                holder.episodeDescription.ellipsize = android.text.TextUtils.TruncateAt.END
                holder.episodeDescriptionExpand.visibility = android.view.View.VISIBLE
                holder.episodeDescriptionCollapse.visibility = android.view.View.GONE
            }
        }
    }

}