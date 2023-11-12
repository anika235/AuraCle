package com.example.auracle.episodecard

import androidx.core.text.parseAsHtml
import androidx.recyclerview.widget.RecyclerView
import com.example.auracle.datapack.listennote.ListenEpisodeShort
import java.text.SimpleDateFormat
import java.util.Date

class EpisodeCardAdapter(
    private val episodeList: ArrayList<ListenEpisodeShort>,
    private val onPlayListener: ((ArrayList<ListenEpisodeShort>, Int) -> Unit)
) : RecyclerView.Adapter<EpisodeCardViewHolder>() {
    override fun onCreateViewHolder(
        parent: android.view.ViewGroup,
        viewType: Int
    ): EpisodeCardViewHolder {
        return EpisodeCardViewHolder(
            android.view.LayoutInflater.from(parent.context)
                .inflate(com.example.auracle.R.layout.episode_card, parent, false)
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
        holder.episodePlay.setOnClickListener {
            onPlayListener(episodeList, position)
        }
        holder.episodeSubscribe.setOnClickListener {

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