package com.example.auracle.episodecard

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.auracle.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.CircularProgressIndicator

class EpisodeCardViewHolder(itemView: View): ViewHolder(itemView) {
    val episodeTitle: TextView = itemView.findViewById(R.id.txtEpisodeTitle)
    val episodeDescription: TextView = itemView.findViewById(R.id.txtEpisodeDescription)
    val episodeDate: TextView = itemView.findViewById(R.id.txtEpisodeDate)
    val episodeDescriptionExpand: TextView = itemView.findViewById(R.id.btnEpisodeExpand)
    val episodeDescriptionCollapse: TextView = itemView.findViewById(R.id.btnEpisodeCollapse)
    val episodePlay: MaterialButton = itemView.findViewById(R.id.btnPlay)
    val episodeDownload: MaterialButton = itemView.findViewById(R.id.btnDownload)
//    val episodeSubscribe: MaterialButton = itemView.findViewById(R.id.btnAddToList)
    val progressIndicator: CircularProgressIndicator = itemView.findViewById(R.id.progressIndicator)
}