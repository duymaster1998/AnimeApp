package edu.nuce.cinema.ui.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.nuce.cinema.data.models.Season
import edu.nuce.cinema.data.models.SeasonDiffCallBack
import edu.nuce.cinema.databinding.ItemSeasonBinding
import javax.inject.Inject

class SeasonAdapter @Inject constructor(
    private val listener: SeasonOnClick
) : ListAdapter<Season, SeasonAdapter.ViewHolder>(SeasonDiffCallBack) {
    inner class ViewHolder(
        private val viewbining: ItemSeasonBinding
    ) : RecyclerView.ViewHolder(viewbining.root) {
        fun bind(
            season: Season,
            listener: SeasonOnClick
        ) {
            viewbining.apply {
                item.setOnClickListener { listener.seasonOnClick(season) }
                tvEpisodeNum.text = season.name
                tvTitle.text = season.title
                episode.text = season.num.toString()
            }
        }
    }

    interface SeasonOnClick {
        fun seasonOnClick(season: Season)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSeasonBinding.inflate(
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