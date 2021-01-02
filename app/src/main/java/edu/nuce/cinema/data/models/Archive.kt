package edu.nuce.cinema.data.models

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Archive(

    @SerializedName("item")
    val item: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("id")
    val id: Int,
): Parcelable
object ArchiveDiffCallBack: DiffUtil.ItemCallback<Archive>() {
    override fun areItemsTheSame(oldItem: Archive, newItem: Archive): Boolean = oldItem.id ==newItem.id
    override fun areContentsTheSame(oldItem: Archive, newItem: Archive): Boolean = oldItem == newItem
}