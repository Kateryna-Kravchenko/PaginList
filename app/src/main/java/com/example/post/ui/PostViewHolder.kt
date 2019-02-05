package com.example.post.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.post.R
import com.example.post.db.Post
import java.text.SimpleDateFormat
import java.util.*

class PostViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.cheese_item, parent, false)
) {

    private val titleView = itemView.findViewById<TextView>(R.id.title)
    private val authorView = itemView.findViewById<TextView>(R.id.author)
    private val timeView = itemView.findViewById<TextView>(R.id.time)
    var post: Post? = null

    fun bindTo(post: Post?) {
        this.post = post
        val formatter = SimpleDateFormat("dd MM yyyy")
        titleView.text = post?.title
        authorView.text = post?.author
        timeView.text = formatter.format(Date(post?.publishedAt ?: 0))

    }
}