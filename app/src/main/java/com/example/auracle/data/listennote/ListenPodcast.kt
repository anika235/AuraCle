package com.example.auracle.data.listennote

import com.google.gson.annotations.SerializedName


data class ListenPodcast(

    var id: String? = null,
    var image: String? = null,
    var genreIds: ArrayList<Int> = arrayListOf(),
    var thumbnail: String? = null,
    var listenScore: Int? = null,
    var titleOriginal: String? = null,
    var listennotesUrl: String? = null,
    var titleHighlighted: String? = null,
    var publisherOriginal: String? = null,
    var publisherHighlighted: String? = null,
    var listenScoreGlobalRank: String? = null

)