package edu.nuce.cinema.data.models.request


import com.google.gson.annotations.SerializedName

data class CommentParams(
    @SerializedName("commentId")
    val commentId: Int,
    @SerializedName("content")
    val content: String,
    @SerializedName("seriesId")
    val seriesId: Int,
    @SerializedName("type")
    val type: String
)