package com.example.auracle.com.example.auracle.api.roomapi

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.auracle.com.example.auracle.datapack.room.RoomEpisode

@Database(entities = [RoomEpisode::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun episodeDao(): EpisodeDao
}