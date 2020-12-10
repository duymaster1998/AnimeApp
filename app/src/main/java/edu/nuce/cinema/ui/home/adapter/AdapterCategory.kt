package edu.nuce.cinema.ui.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import edu.nuce.cinema.R
import edu.nuce.cinema.data.models.Category
import edu.nuce.cinema.data.models.CategoryDiffCallback
import edu.nuce.cinema.databinding.ItemSeriesBinding
import javax.inject.Inject

class AdapterCategory @Inject constructor(
    private val listener: OnClickCategory,
    private val requestManager: RequestManager,
) : ListAdapter<Category, AdapterCategory.ViewHolder>(CategoryDiffCallback) {

    interface OnClickCategory{
        fun onClikCategory(view: View, category: Category)
    }

    class ViewHolder(
        private val requestManager: RequestManager,
        private val viewBinding: ItemSeriesBinding
    ) : RecyclerView.ViewHolder(viewBinding.root) {
        @SuppressLint("UseCompatLoadingForDrawables", "WrongConstant")
        fun bind(
            category: Category,
            listener:OnClickCategory
        ) {
            viewBinding.run {
                item.setOnClickListener { listener.onClikCategory(it,category) }
                ivSeries.layoutParams.width = root.resources.getDimension(R.dimen._60sdp).toInt()
                ivSeries.layoutParams.height = root.resources.getDimension(R.dimen._60sdp).toInt()
                requestManager.load(category.image)
                    .circleCrop()
                    .into(ivSeries)
                tvName.apply {
                    text = category.name
                    textAlignment = 4
                    maxLines = 1
                }
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
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it,listener)
        }
    }
}