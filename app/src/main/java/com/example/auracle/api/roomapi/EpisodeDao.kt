package com.example.auracle.com.example.auracle.api.roomapi

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.auracle.com.example.auracle.datapack.room.RoomEpisode
import java.io.InputStream

@Dao
interface EpisodeDao {
    @Query("select * from roomepisode")
    fun getAll(): List<RoomEpisode>

    @Query("select * from roomepisode where id = :id")
    fun getEpisode(id: String): RoomEpisode

    @Query("select * from roomepisode where podcastId = :podcastId")
    fun getPodcast(podcastId: String): List<RoomEpisode>

    @Insert
    fun insertAll(vararg episodes: RoomEpisode)

    @Insert
    fun insert(episode: RoomEpisode)

    @Query("delete from roomepisode where id = :id")
    fun delete(id: String)

//    @Delete
//    fun delete(episode: RoomEpisode)
}