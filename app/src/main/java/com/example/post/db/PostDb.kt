package com.example.post.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.post.ulit.ioThread

@Database(entities = arrayOf(Post::class), version = 1)
abstract class PostDb : RoomDatabase() {
    abstract fun postDao(): PostDao

    companion object {
        private var instance: PostDb? = null
        @Synchronized
        fun get(context: Context): PostDb {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    PostDb::class.java, "PostDatabase"
                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            fillInDb(context.applicationContext)
                        }
                    }).build()
            }
            return instance!!
        }

        private fun fillInDb(context: Context) {
            ioThread {
                val time = System.currentTimeMillis()
                val list = MutableList(100) { index ->
                    Post(index, "Post $index", "Author $index", time)
                }
                get(context).postDao().insert(list)
            }
        }
    }
}
