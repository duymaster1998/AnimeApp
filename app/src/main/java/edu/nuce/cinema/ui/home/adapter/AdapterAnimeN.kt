package edu.nuce.cinema.ui.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import edu.nuce.cinema.R
import edu.nuce.cinema.data.models.Anime
import edu.nuce.cinema.data.models.AnimeDiffCallBack
import edu.nuce.cinema.databinding.ItemSeriesBinding
import javax.inject.Inject

class AdapterAnimeN @Inject constructor(
    private val listener: OnClickAnime,
    private val requestManager: RequestManager,
) : ListAdapter<Anime, AdapterAnimeN.ViewHolder>(AnimeDiffCallBack) {

    interface OnClickAnime {
        fun onClickAnimeN(view: View, anime: Anime)
    }

    class ViewHolder(
        private val requestManager: RequestManager,
        private val viewBinding: ItemSeriesBinding,
    ) : RecyclerView.ViewHolder(viewBinding.root) {
        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(
            anime: Anime,
            listener: OnClickAnime,
        ) {
            viewBinding.run {
                val name = "( Episode " + anime.episode + " ) " +anime.title
                ivSeries.layoutParams.width = root.resources.getDimension(R.dimen._100sdp).toInt()
                ivSeries.layoutParams.height = root.resources.getDimension(R.dimen._100sdp).toInt()
                item.setOnClickListener { listener.onClickAnimeN(it, anime) }
                requestManager.load(anime.image)
                    .into(ivSeries)
                tvName.text = name
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
            holder.bind(it, listener)
        }
    }
}