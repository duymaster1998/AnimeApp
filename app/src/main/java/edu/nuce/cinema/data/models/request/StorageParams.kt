package edu.nuce.cinema.data.models.request


import com.google.gson.annotations.SerializedName

data class StorageParams(
    @SerializedName("archiveId")
    val archiveId: Int,
    @SerializedName("episode")
    val episode: Int,
    @SerializedName("seriesId")
    val seriesId: Int,
    @SerializedName("type")
    val type: String
)