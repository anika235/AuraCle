package com.example.auracle.datapack.listennote

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ListenEpisodeShort (
    @SerializedName("id"                   ) var id                 : String?  = null,
    @SerializedName("link"                 ) var link               : String?  = null,
    @SerializedName("audio"                ) var audio              : String?  = null,
    @SerializedName("image"                ) var image              : String?  = null,
    @SerializedName("title"                ) var title              : String?  = null,
    @SerializedName("thumbnail"            ) var thumbnail          : String?  = null,
    @SerializedName("description"          ) var description        : String?  = null,
    @SerializedName("pub_date_ms"          ) var pubDateMs          : Long?    = null,
    @SerializedName("guid_from_rss"        ) var guidFromRss        : String?  = null,
    @SerializedName("listennotes_url"      ) var listennotesUrl     : String?  = null,
    @SerializedName("audio_length_sec"     ) var audioLengthSec     : Int?     = null,
    @SerializedName("explicit_content"     ) var explicitContent    : Boolean? = null,
    @SerializedName("maybe_audio_invalid"  ) var maybeAudioInvalid  : Boolean? = null,
    @SerializedName("listennotes_edit_url" ) var listennotesEditUrl : String?  = null,
    @SerializedName("offline_location"     ) var offlineLocation    : String?  = null,
    @SerializedName("is_downloading"       ) var isDownloading      : Boolean  = false
): Parcelable