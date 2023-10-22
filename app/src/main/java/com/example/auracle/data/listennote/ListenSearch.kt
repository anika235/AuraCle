package com.example.auracle.data.listennote

import com.google.gson.annotations.SerializedName


data class ListenSearch(

    var took: Double? = null,
    var count: Int? = null,
    var total: Int? = null,
    var results: ArrayList<ListenSearchResult> = arrayListOf(),
    var nextOffset: Int? = null

)