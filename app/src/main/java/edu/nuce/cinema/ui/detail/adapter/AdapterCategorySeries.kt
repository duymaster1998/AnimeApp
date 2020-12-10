package edu.nuce.cinema.ui.detail.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import edu.nuce.cinema.data.models.Category
import edu.nuce.cinema.data.models.CategoryDiffCallback
import edu.nuce.cinema.databinding.ItemCategoryBinding
import javax.inject.Inject

class AdapterCategorySeries @Inject constructor(
//    private val listener: CategoryOnClick,
    private val requestManager: RequestManager,
) : ListAdapter<Category, AdapterCategorySeries.ViewHolder>(CategoryDiffCallback) {

//    interface CategoryOnClick{
//        fun CategoryOnClick(view: View, category: Category)
//    }

    class ViewHolder(
        private val requestManager: RequestManager,
        private val viewBinding: ItemCategoryBinding
    ) : RecyclerView.ViewHolder(viewBinding.root) {
        @SuppressLint("UseCompatLoadingForDrawables", "WrongConstant")
        fun bind(
            category: Category,
//            listener:CategoryOnClick
        ) {
            viewBinding.run {
//                item.setOnClickListener { listener.CategoryOnClick(it,category) }
                tvCategory.text = category.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            requestManager,
            ItemCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position).let {
            holder.bind(it)
        }
    }
}