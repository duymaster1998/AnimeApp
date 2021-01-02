package edu.nuce.cinema.data.models


import com.google.gson.annotations.SerializedName

data class Report(
    @SerializedName("commentId")
    val commentId: Int,
    @SerializedName("detail")
    val detail: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("status")
    val status: Int,
    @SerializedName("title")
    val title: Int,
    @SerializedName("updatedAt")
    val updatedAt: String
)