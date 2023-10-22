package com.example.auracle.api

import com.example.auracle.data.listennote.ListenSearch
import com.example.auracle.data.listennote.ListenSearchResult
import org.json.JSONString
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.QueryMap

const val apiKey = "6c993ea9a3eb4093bf1957be361c155a"

interface ListenNoteRoutes {

//    @Headers("X-ListenAPI-Key: $apiKey")
    @GET("search")
    fun search(@QueryMap options: Map<String, String>): Call<ListenSearch>
}