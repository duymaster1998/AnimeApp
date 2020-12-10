package edu.nuce.cinema.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import edu.nuce.cinema.data.models.Series
import edu.nuce.cinema.databinding.ItemSliderBinding
import javax.inject.Inject

class SliderAdapter @Inject constructor(
    private val series:List<Series>,
    private val requestManager: RequestManager,
) : RecyclerView.Adapter<SliderAdapter.ViewHolder>() {

    inner class ViewHolder(
        private val requestManager: RequestManager,
        private val viewBinding: ItemSliderBinding
    ) : RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(
            series: Series
        ) {
            viewBinding.run {
                requestManager.load(series.backdropPath)
                    .into(image)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            requestManager,
            ItemSliderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(series.get(position))
    }

    override fun getItemCount(): Int {
        return series.size
    }
}