package edu.nuce.cinema.ui.common.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import edu.nuce.cinema.R
import edu.nuce.cinema.data.models.Comment
import edu.nuce.cinema.data.models.CommentDiffCallBack
import edu.nuce.cinema.databinding.ItemCommentBinding
import edu.nuce.cinema.ui.common.events.CommentEvent
import javax.inject.Inject

class CommentAdapter @Inject constructor(
    private val listener: CommentEvent,
    private val requestManager: RequestManager,
    private val list: List<Int>,
    private val isHide:Boolean
) : ListAdapter<Comment, CommentAdapter.ViewHolder>(CommentDiffCallBack) {
    inner class ViewHolder(
        private val viewbining: ItemCommentBinding
    ) : RecyclerView.ViewHolder(viewbining.root) {
        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(
            comment: Comment,
            listener: CommentEvent
        ) {
            val name = comment.lastName + " " + comment.firstName
            viewbining.apply {
                if (check(list,comment) == true) {
                    btnLike.setImageDrawable(root.resources.getDrawable(R.drawable.ic_like_color))
                }
                else {
                    btnLike.setImageDrawable(root.resources.getDrawable(R.drawable.ic_like))
                }
                requestManager.load(comment.avatar)
                    .circleCrop()
                    .into(avatar)
                tvName.text = name
                tvDate.text = comment.updatedAt
                tvComment.text = comment.content
                tvLike.text = comment.like
                btnLike.setOnClickListener {
                    listener.onLike(comment) }
                ivCmt.setOnClickListener { listener.onParentComment(comment) }
                ivReport.setOnClickListener { listener.onReport(comment) }
                if (isHide)
                    ivCmt.visibility = View.GONE
            }
        }
    }
    fun check(list: List<Int>,comment: Comment):Boolean{
        if (list.size ==0)
            return false
        list.forEach { item -> if(item == comment.id.toInt() )
            return true
        }
        return false
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCommentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position).let {
            holder.bind(it, listener)
        }
    }
}