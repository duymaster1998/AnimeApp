package edu.nuce.cinema.ui.chapter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import edu.nuce.cinema.data.models.*
import edu.nuce.cinema.databinding.ItemCategoryBinding
import edu.nuce.cinema.databinding.ItemChapterBinding
import edu.nuce.cinema.databinding.ItemEpisodeBinding
import javax.inject.Inject

class AdapterChapter @Inject constructor(
    private val listener: MangaOnClick,
) : ListAdapter<Manga, AdapterChapter.ViewHolder>(MangaDiffCallBack) {

    interface MangaOnClick {
        fun MangaOnClick(manga: Manga)
        fun onLongClick(manga: Manga):Boolean
    }

    class ViewHolder(
        private val viewBinding: ItemChapterBinding
    ) : RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(
            manga: Manga,
            listener:MangaOnClick
        ) {
            viewBinding.run {
                item.setOnClickListener { listener.MangaOnClick(manga) }
                item.setOnLongClickListener { listener.onLongClick(manga) }
                tvChapterNum.text = manga.episode.toString()
                tvDate.text = manga.updatedAt
                tvView.text = manga.view
                chapterName.text = manga.title
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemChapterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position).let {
            holder.bind(it,listener)
        }
    }
}