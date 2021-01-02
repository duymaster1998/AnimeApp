package edu.nuce.cinema.data.models


import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Series(
    @SerializedName("id")
    val id: Int,
    @SerializedName("backdropPath")
    val backdropPath: String,
    @SerializedName("posterPath")
    val posterPath: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("sumary")
    val sumary: String,
    @SerializedName("rating")
    val rating: Float,
    @SerializedName("author")
    val author: String,
    @SerializedName("updatedAt")
    val updatedAt: String
):Parcelable

object SeriesDiffCallback : DiffUtil.ItemCallback<Series>() {
    override fun areItemsTheSame(oldItem: Series, newItem: Series): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Series, newItem: Series): Boolean = oldItem == newItem

}