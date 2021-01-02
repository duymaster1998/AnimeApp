package edu.nuce.cinema.data.models


import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Manga(
    @SerializedName("episode")
    val episode: Int,
    @SerializedName("file")
    val `file`: String,
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
    @SerializedName("view")
    val view: String
):Parcelable
object MangaDiffCallBack: DiffUtil.ItemCallback<Manga>() {
    override fun areItemsTheSame(oldItem: Manga, newItem: Manga): Boolean = oldItem.id ==newItem.id


    override fun areContentsTheSame(oldItem: Manga, newItem: Manga): Boolean = oldItem == newItem
}