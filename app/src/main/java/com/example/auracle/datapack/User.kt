package com.example.auracle.com.example.auracle.datapack

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("email"     ) var email: String,
    @SerializedName("name"      ) var name: String,
    @SerializedName("password"  ) var password: String,
    @SerializedName("uid"       ) var uid: String
)
