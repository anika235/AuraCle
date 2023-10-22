package com.example.auracle.api

import android.util.Log
import com.example.auracle.datapack.listennote.ListenPodcastShort
import com.example.auracle.datapack.listennote.ListenSearch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListenNoteApi {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://listen-api-test.listennotes.com/api/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ListenNoteRoutes::class.java)

    fun search(query: String): ArrayList<ListenPodcastShort>? {
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

}