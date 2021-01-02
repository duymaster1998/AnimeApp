package edu.nuce.cinema.ui.series.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import edu.nuce.cinema.data.models.Series
import edu.nuce.cinema.data.models.SeriesDiffCallback
import edu.nuce.cinema.databinding.ItemSeries2Binding
import edu.nuce.cinema.databinding.ItemSeriesBinding
import javax.inject.Inject

class AdapterSeries2 @Inject constructor(
    private val listener: SeriesOnClick,
    private val requestManager: RequestManager,
) : ListAdapter<Series, AdapterSeries2.ViewHolder>(SeriesDiffCallback) {

    interface SeriesOnClick{
        fun onClickSeries(series: Series)
    }

    class ViewHolder(
        private val requestManager: RequestManager,
        private val viewBinding: ItemSeries2Binding,
    ) : RecyclerView.ViewHolder(viewBinding.root) {
        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(
            series: Series,
            listener:SeriesOnClick,
            ) {
            viewBinding.run {
                item.setOnClickListener { listener.onClickSeries(series) }
                requestManager.load(series.posterPath)
                    .into(ivSeries)
                tvName.text = series.name
                tvAuthor.text = series.author
                ratingBar.rating = series.rating
                tvDate.text = series.updatedAt
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            requestManager,
            ItemSeries2Binding.inflate(
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