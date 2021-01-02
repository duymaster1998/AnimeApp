package edu.nuce.cinema.data.models.request


import com.google.gson.annotations.SerializedName

data class ReportParams(
    @SerializedName("commentId")
    val commentId: Int,
    @SerializedName("detail")
    val detail: String,
    @SerializedName("title")
    val title: String
)