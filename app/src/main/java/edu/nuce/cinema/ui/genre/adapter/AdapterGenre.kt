package edu.nuce.cinema.ui.genre.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import edu.nuce.cinema.data.models.Category
import edu.nuce.cinema.data.models.CategoryDiffCallback
import edu.nuce.cinema.databinding.ItemCategory2Binding
import javax.inject.Inject

class AdapterGenre @Inject constructor(
    private val listener: GenreOnClick,
    private val requestManager: RequestManager,
) : ListAdapter<Category, AdapterGenre.ViewHolder>(CategoryDiffCallback) {

    interface GenreOnClick {
        fun onClickGenre(category: Category)
    }

    class ViewHolder(
        private val requestManager: RequestManager,
        private val viewBinding: ItemCategory2Binding,
    ) : RecyclerView.ViewHolder(viewBinding.root) {
        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(
            category: Category,
            listener: GenreOnClick,
        ) {
            viewBinding.run {
                item.setOnClickListener { listener.onClickGenre(category) }
                requestManager.load(category.image)
                    .into(ivCategory)
                tvName.text = category.name
                tvItem.text = category.item + " item"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            requestManager,
            ItemCategory2Binding.inflate(
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