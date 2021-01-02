package edu.nuce.cinema.ui.library.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.nuce.cinema.data.models.*
import edu.nuce.cinema.databinding.ItemArchiveBinding
import edu.nuce.cinema.ui.library.LibraryEvents
import javax.inject.Inject

class ArchiveAdapter @Inject constructor(
    private val listener: LibraryEvents,
) : ListAdapter<Archive, ArchiveAdapter.ViewHolder>(ArchiveDiffCallBack) {



    inner class ViewHolder(
        private val viewbining: ItemArchiveBinding
    ) : RecyclerView.ViewHolder(viewbining.root) {
        fun bind(
            archive: Archive,
            listener: LibraryEvents
        ) {
            val itemNum = archive.item + " item"
            viewbining.apply {
                item.setOnClickListener { listener.onClickArchive(archive) }
                item.setOnLongClickListener { listener.onLongClickArchive(archive) }
                tvName.text = archive.name
                tvNumItem.text = itemNum
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemArchiveBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position).let {
            holder.bind(it, listener)
        }
    }
}