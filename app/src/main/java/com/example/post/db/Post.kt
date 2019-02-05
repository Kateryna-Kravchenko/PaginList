package com.example.post.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Post(
    @PrimaryKey val id: Int, val title: String,
    val author: String,
    val publishedAt: Long
)