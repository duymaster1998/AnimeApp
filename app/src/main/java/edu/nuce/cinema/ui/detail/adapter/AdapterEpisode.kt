package edu.nuce.cinema.ui.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import edu.nuce.cinema.data.models.Anime
import edu.nuce.cinema.data.models.AnimeDiffCallBack
import edu.nuce.cinema.data.models.Category
import edu.nuce.cinema.data.models.CategoryDiffCallback
import edu.nuce.cinema.databinding.ItemCategoryBinding
import edu.nuce.cinema.databinding.ItemEpisodeBinding
import javax.inject.Inject

class AdapterEpisode @Inject constructor(
    private val listener: AnimeOnClick,
    private val requestManager: RequestManager,
) : ListAdapter<Anime, AdapterEpisode.ViewHolder>(AnimeDiffCallBack) {

    interface AnimeOnClick {
        fun AnimeOnClick(anime: Anime)
    }

    class ViewHolder(
        private val requestManager: RequestManager,
        private val viewBinding: ItemEpisodeBinding
    ) : RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(
            anime: Anime,
            listener:AnimeOnClick
        ) {
            viewBinding.run {
                item.setOnClickListener { listener.AnimeOnClick(anime) }
                tvName.text = anime.title
                tvEpisode.text = anime.episode.toString()
                tvView.text = anime.view
                tvDate.text = anime.updatedAt
                requestManager.load(anime.image).into(image)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            requestManager,
            ItemEpisodeBinding.inflate(
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