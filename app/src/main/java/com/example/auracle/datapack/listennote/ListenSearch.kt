package com.example.auracle.datapack.listennote

import com.google.gson.annotations.SerializedName


data class ListenSearch(

    @SerializedName("took"        ) var took: Double? = null,
    @SerializedName("count"       ) var count: Int? = null,
    @SerializedName("total"       ) var total: Int? = null,
    @SerializedName("results"     ) var results: ArrayList<ListenPodcastShort> = arrayListOf(),
    @SerializedName("next_offset" ) var nextOffset: Int? = null

)