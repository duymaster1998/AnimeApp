package edu.nuce.cinema.ui.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import edu.nuce.cinema.data.models.Series
import edu.nuce.cinema.data.models.SeriesDiffCallback
import edu.nuce.cinema.databinding.ItemSeriesBinding
import javax.inject.Inject

class AdapterAnime @Inject constructor(
    private val listener: OnClickAnime,
    private val requestManager: RequestManager,
) : ListAdapter<Series, AdapterAnime.ViewHolder>(SeriesDiffCallback) {

    interface OnClickAnime{
        fun onClickAnime(view: View,series: Series)
    }

    class ViewHolder(
        private val requestManager: RequestManager,
        private val viewBinding: ItemSeriesBinding,
    ) : RecyclerView.ViewHolder(viewBinding.root) {
        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(
            series: Series,
            listener: OnClickAnime,
            ) {
            viewBinding.run {
                item.setOnClickListener { listener.onClickAnime(it,series) }
                requestManager.load(series.backdropPath)
                    .into(ivSeries)
                tvName.text = series.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            requestManager,
            ItemSeriesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it,listener)
        }
    }
}