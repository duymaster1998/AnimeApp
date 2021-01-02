package edu.nuce.cinema.data.models


import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Comment(
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("like")
    val like: String,
    @SerializedName("seriesId")
    val seriesId: String,
    @SerializedName("updatedAt")
    val updatedAt: String
) : Parcelable

object CommentDiffCallBack : DiffUtil.ItemCallback<Comment>() {
    override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean =
        oldItem == newItem
}