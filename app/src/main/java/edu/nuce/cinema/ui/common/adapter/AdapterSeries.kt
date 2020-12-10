package edu.nuce.cinema.ui.common.adapter

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

class AdapterSeries @Inject constructor(
    private val listener: SeriesOnClick,
    private val requestManager: RequestManager,
    private val width:Int,
    private val height: Int
) : ListAdapter<Series, AdapterSeries.ViewHolder>(SeriesDiffCallback) {

    interface SeriesOnClick{
        fun onClickSeries(view: View,series: Series)
    }

    class ViewHolder(
        private val requestManager: RequestManager,
        private val viewBinding: ItemSeriesBinding,
        private val width:Int,
        private val height:Int
    ) : RecyclerView.ViewHolder(viewBinding.root) {
        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(
            series: Series,
            listener:SeriesOnClick,
            ) {
            viewBinding.run {
                item.setOnClickListener { listener.onClickSeries(it,series) }
                ivSeries.layoutParams.width =width
                ivSeries.layoutParams.height = height
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
            width,
            height,
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it,listener)
        }
    }
}