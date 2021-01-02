package edu.nuce.cinema.data.models


import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Season(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("num")
    val num: Int
) : Parcelable


@Parcelize
class Seasons : ArrayList<Season>(), Parcelable

object SeasonDiffCallBack : DiffUtil.ItemCallback<Season>() {
    override fun areItemsTheSame(oldItem: Season, newItem: Season): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Season, newItem: Season): Boolean = oldItem == newItem
}