package com.example.post.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.post.db.Post


@Dao
interface PostDao {
    @Query("SELECT * FROM Post WHERE id < :startPosition ORDER BY id DESC LIMIT :loadSize")
    fun allPostByPosition(startPosition: Int, loadSize: Int): List<Post>

    @Insert
    fun insert(posts: List<Post>)

    @Query("SELECT MAX(id) +1 FROM Post")
    fun getLastIndex(): Int

    @Query("DELETE FROM Post")
    fun deleteAllPost()
}