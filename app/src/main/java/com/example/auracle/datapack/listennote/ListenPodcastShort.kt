package com.example.auracle.datapack.listennote

import com.google.gson.annotations.SerializedName


data class ListenPodcastShort(

    @SerializedName("rss"                      ) var rss                    : String?        = null,
    @SerializedName("description_highlighted"  ) var descriptionHighlighted : String?        = null,
    @SerializedName("description_original"     ) var descriptionOriginal    : String?        = null,
    @SerializedName("title_highlighted"        ) var titleHighlighted       : String?        = null,
    @SerializedName("title_original"           ) var titleOriginal          : String?        = null,
    @SerializedName("publisher_highlighted"    ) var publisherHighlighted   : String?        = null,
    @SerializedName("publisher_original"       ) var publisherOriginal      : String?        = null,
    @SerializedName("image"                    ) var image                  : String?        = null,
    @SerializedName("thumbnail"                ) var thumbnail              : String?        = null,
    @SerializedName("itunes_id"                ) var itunesId               : Int?           = null,
    @SerializedName("latest_episode_id"        ) var latestEpisodeId        : String?        = null,
    @SerializedName("latest_pub_date_ms"       ) var latestPubDateMs        : Long?          = null,
    @SerializedName("earliest_pub_date_ms"     ) var earliestPubDateMs      : Long?          = null,
    @SerializedName("id"                       ) var id                     : String?        = null,
    @SerializedName("genre_ids"                ) var genreIds               : ArrayList<Int> = arrayListOf(),
    @SerializedName("listennotes_url"          ) var listennotesUrl         : String?        = null,
    @SerializedName("total_episodes"           ) var totalEpisodes          : Int?           = null,
    @SerializedName("audio_length_sec"         ) var audioLengthSec         : Int?           = null,
    @SerializedName("update_frequency_hours"   ) var updateFrequencyHours   : Int?           = null,
    @SerializedName("email"                    ) var email                  : String?        = null,
    @SerializedName("explicit_content"         ) var explicitContent        : Boolean?       = null,
    @SerializedName("website"                  ) var website                : String?        = null,
    @SerializedName("listen_score"             ) var listenScore            : String?        = null,
    @SerializedName("listen_score_global_rank" ) var listenScoreGlobalRank  : String?        = null


)