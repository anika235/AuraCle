package com.example.auracle.com.example.auracle.datapack.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RoomEpisode(
    @PrimaryKey val id: String,
    val podcastId: String,
    val audioLocation: String,
)