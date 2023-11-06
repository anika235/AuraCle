package com.example.auracle.api

import android.util.Log
import com.example.auracle.datapack.listennote.ListenPodcastLong
import com.example.auracle.datapack.listennote.ListenSearchPodcast
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListenNoteApi {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://listen-api-test.listennotes.com/api/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ListenNoteRoutes::class.java)

    fun search(query: String): ArrayList<ListenSearchPodcast>? {
        val options = mapOf(
            "q" to query,
            "type" to "podcast"
        )
        val searchResult = retrofit.search(options).execute()
        return try {
//            Log.d("ListenNoteApi", "Response: ${searchResult.body()}")
            searchResult.body()?.results

        } catch (e: Exception) {
            Log.e("ListenNoteApi", "Error: ${e.message}")
            null
        }
    }

    fun podcastDetails(id: String): ListenPodcastLong {
        val podcastDetails = retrofit.podcastDetail(id).execute()
        return try {
            podcastDetails.body()!!
        } catch (e: Exception) {
            Log.e("ListenNoteApi", "Error: ${e.message}")
            ListenPodcastLong()
        }
    }

    fun bestSearch(byGenre: String? = null): ArrayList<ListenSearchPodcast> {

        val options = if(byGenre != null) mapOf(
            "genre_id" to byGenre,
        ) else mapOf()
        val bestSearch = retrofit.bestPodcasts(options).execute()
        val podcasts = bestSearch.body()?.podcasts
        val podcastList = podcasts?.map { ListenSearchPodcast(it) }
        return try {
            podcastList as ArrayList<ListenSearchPodcast>
        } catch (e: Exception) {
            Log.e("ListenNoteApi", "Error: ${e.message}")
            ArrayList()
        }
    }

}