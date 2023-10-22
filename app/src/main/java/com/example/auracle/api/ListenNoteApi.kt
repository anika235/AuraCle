package com.example.auracle.api

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListenNoteApi {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://listen-api-test.listennotes.com/api/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ListenNoteRoutes::class.java)

    fun search(query: String) {
        val options = mapOf(
            "q" to query
        )
Log.d("ListenNoteApi", options.toString())
        val searchResult = retrofit.search(options).execute()
Log.d("ListenNoteApi", searchResult.body()?.results.toString())

    }

}