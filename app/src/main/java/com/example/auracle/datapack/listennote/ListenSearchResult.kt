package com.example.auracle.datapack.listennote


data class ListenSearchResult(

    var id: String? = null,
    var rss: String? = null,
    var link: String? = null,
    var audio: String? = null,
    var image: String? = null,
    var podcast: ListenPodcast? = ListenPodcast(),
    var itunesId: Int? = null,
    var thumbnail: String? = null,
    var pubDateMs: Int? = null,
    var guidFromRss: String? = null,
    var titleOriginal: String? = null,
    var listennotesUrl: String? = null,
    var audioLengthSec: Int? = null,
    var explicitContent: Boolean? = null,
    var titleHighlighted: String? = null,
    var descriptionOriginal: String? = null,
    var descriptionHighlighted: String? = null,
    var transcriptsHighlighted: ArrayList<String> = arrayListOf()

)