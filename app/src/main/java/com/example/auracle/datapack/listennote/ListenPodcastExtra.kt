package com.example.auracle.datapack.listennote

import com.google.gson.annotations.SerializedName

data class ListenPodcastExtra(
    @SerializedName("url1"             ) var url1            : String? = null,
    @SerializedName("url2"             ) var url2            : String? = null,
    @SerializedName("url3"             ) var url3            : String? = null,
    @SerializedName("google_url"       ) var googleUrl       : String? = null,
    @SerializedName("spotify_url"      ) var spotifyUrl      : String? = null,
    @SerializedName("youtube_url"      ) var youtubeUrl      : String? = null,
    @SerializedName("linkedin_url"     ) var linkedinUrl     : String? = null,
    @SerializedName("wechat_handle"    ) var wechatHandle    : String? = null,
    @SerializedName("patreon_handle"   ) var patreonHandle   : String? = null,
    @SerializedName("twitter_handle"   ) var twitterHandle   : String? = null,
    @SerializedName("facebook_handle"  ) var facebookHandle  : String? = null,
    @SerializedName("amazon_music_url" ) var amazonMusicUrl  : String? = null,
    @SerializedName("instagram_handle" ) var instagramHandle : String? = null
)
