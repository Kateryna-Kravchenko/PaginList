package com.example.post.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.example.post.db.MyPositionalDataSource
import com.example.post.db.Post
import com.example.post.db.PostDb
import com.example.post.ulit.MainThreadExecutor
import com.example.post.ulit.ioThread
import com.example.post.ulit.ioThreadDelay
import java.util.concurrent.Executors.newSingleThreadExecutor

class PostViewModel(app: Application) : AndroidViewModel(app) {
    val dao = PostDb.get(app).postDao()
    val changeNotifier = MutableLiveData<Int>()
    val pagedList = MutableLiveData<PagedList<Post>>()
    var error = MutableLiveData<Boolean>()
    var isFirst = MutableLiveData<Boolean>(true)

    fun increment(count: Int) {
        MainThreadExecutor().execute { changeNotifier.value = changeNotifier.value?.plus(count) }
    }

    fun deleteAll() {
        ioThread {
            dao.deleteAllPost()
        }
    }

    fun insertNew(context: Context) {
        ioThreadDelay {
            val postPosition = dao.getLastIndex()
            val list = MutableList(5) { index ->
                Post(
                    index + postPosition,
                    "Post ${index + postPosition}",
                    "Author ${index + postPosition}",
                    System.currentTimeMillis()
                )
            }
            dao.insert(list)
            increment(5)
            insertNew(context)
        }
    }

    fun refreshList() {
        if (isFirst.value!!) {
            error.value = true
            ioThread { dao.getLastIndex() }
        } else {
            ioThread {
                val pagedListConfig = PagedList.Config.Builder()
                    .setMaxSize(40).setPageSize(10)
                    .setEnablePlaceholders(false)
                    .build()
                val pageList = PagedList.Builder(MyPositionalDataSource(dao), pagedListConfig)
                    .setFetchExecutor(newSingleThreadExecutor())
                    .setNotifyExecutor(MainThreadExecutor())
                    .build()
                MainThreadExecutor().execute { changeNotifier.value = 0 }
                MainThreadExecutor().execute { error.value = false }
                MainThreadExecutor().execute { pagedList.value = pageList }
            }
        }
    }
}
