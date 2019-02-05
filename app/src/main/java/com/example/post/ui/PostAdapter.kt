package com.example.post.ui

import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import android.view.ViewGroup
import com.example.post.db.Post

class PostAdapter : PagedListAdapter<Post, PostViewHolder>(diffCallback) {
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder =
        PostViewHolder(parent)

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean =
                    oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean =
                    oldItem == newItem
        }
    }
}
