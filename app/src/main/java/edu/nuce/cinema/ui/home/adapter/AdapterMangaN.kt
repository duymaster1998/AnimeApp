package edu.nuce.cinema.ui.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import edu.nuce.cinema.R
import edu.nuce.cinema.data.models.Manga
import edu.nuce.cinema.data.models.MangaDiffCallBack
import edu.nuce.cinema.databinding.ItemSeriesBinding
import javax.inject.Inject

class AdapterMangaN @Inject constructor(
    private val listener: OnClickManga,
    private val requestManager: RequestManager,
) : ListAdapter<Manga, AdapterMangaN.ViewHolder>(MangaDiffCallBack) {

    interface OnClickManga {
        fun onClickMangaN(view: View, manga: Manga)
    }

    class ViewHolder(
        private val requestManager: RequestManager,
        private val viewBinding: ItemSeriesBinding,
    ) : RecyclerView.ViewHolder(viewBinding.root) {
        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(
            Manga: Manga,
            listener: OnClickManga,
        ) {
            viewBinding.run {
                val name =  "( Chapter " + Manga.episode + " ) "+Manga.title
                ivSeries.layoutParams.width = root.resources.getDimension(R.dimen._100sdp).toInt()
                ivSeries.layoutParams.height = root.resources.getDimension(R.dimen._100sdp).toInt()
                item.setOnClickListener { listener.onClickMangaN(it, Manga) }
                requestManager.load(Manga.image)
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