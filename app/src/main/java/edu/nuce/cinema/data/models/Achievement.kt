package edu.nuce.cinema.data.models


import com.google.gson.annotations.SerializedName

data class Achievement(
    @SerializedName("content")
    val content: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("pointUpgrade")
    val pointUpgrade: String,
    @SerializedName("type")
    val type: String
)