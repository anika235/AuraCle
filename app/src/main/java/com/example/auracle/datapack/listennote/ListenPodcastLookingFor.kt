package com.example.auracle.datapack.listennote

import com.google.gson.annotations.SerializedName

data class ListenPodcastLookingFor(
    @SerializedName("guests"          ) var guests         : Boolean? = null,
    @SerializedName("cohosts"         ) var cohosts        : Boolean? = null,
    @SerializedName("sponsors"        ) var sponsors       : Boolean? = null,
    @SerializedName("cross_promotion" ) var crossPromotion : Boolean? = null
)
