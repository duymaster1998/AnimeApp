package edu.nuce.cinema.data.models


import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Anime(
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("deletedAt")
    val deletedAt: String,
    @SerializedName("episode")
    val episode: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("pointDownload")
    val pointDownload: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("video")
    val video: String,
    @SerializedName("view")
    val view: String
):Parcelable

object AnimeDiffCallBack: DiffUtil.ItemCallback<Anime>() {
    override fun areItemsTheSame(oldItem: Anime, newItem: Anime): Boolean = oldItem.id ==newItem.id


    override fun areContentsTheSame(oldItem: Anime, newItem: Anime): Boolean = oldItem == newItem
}