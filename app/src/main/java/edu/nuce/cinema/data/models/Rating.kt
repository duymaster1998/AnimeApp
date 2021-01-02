package edu.nuce.cinema.data.models


import com.google.gson.annotations.SerializedName

data class Rating(
    @SerializedName("rating")
    val rating: Int,
    @SerializedName("seriesId")
    val seriesId: Int,
)