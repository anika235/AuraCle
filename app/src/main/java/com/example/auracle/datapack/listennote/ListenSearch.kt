package com.example.auracle.datapack.listennote


data class ListenSearch(

    var took: Double? = null,
    var count: Int? = null,
    var total: Int? = null,
    var results: ArrayList<ListenSearchResult> = arrayListOf(),
    var nextOffset: Int? = null

)