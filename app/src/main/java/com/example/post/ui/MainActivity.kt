package com.example.post.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import com.example.post.R
import com.example.post.db.Post
import com.example.post.ulit.MainThreadExecutor
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProviders.of(this).get(PostViewModel::class.java)
    }

    private var adapter: PostAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel.dao
        adapter = PostAdapter()
        initButtonListener()
        postList.adapter = adapter
        viewModel.refreshList()
        refresh.setOnRefreshListener {
            refresh.isRefreshing = true
            viewModel.refreshList()
        }
        cvNewPostsCount.setOnClickListener {
            viewModel.refreshList()
        }
        viewModel.changeNotifier.observe(this,
            Observer<Int> { value -> value?.let { showToast(value) } })
        viewModel.error.observe(
            this,
            Observer<Boolean> { value ->
                if (value) {
                    reloadButton.visibility = View.VISIBLE
                    errorTextView.visibility = View.VISIBLE
                } else {
                    reloadButton.visibility = View.GONE
                    errorTextView.visibility = View.GONE
                }
            })
        viewModel.pagedList.observe(this, Observer<PagedList<Post>> { value ->
            value?.let {
                MainThreadExecutor().execute {
                    adapter?.submitList(value)
                    postList.smoothScrollToPosition(0)
                    refresh.isRefreshing = false
                }
            }
        })
    }

    private fun initButtonListener() {
        deleteButton.setOnClickListener {
            viewModel.deleteAll()
            viewModel.refreshList()
        }
        reloadButton.setOnClickListener {
            if (viewModel.isFirst.value!!) {
                viewModel.isFirst.value = false
                viewModel.insertNew(context = this)
            }
            viewModel.refreshList()
        }
    }

    private fun showToast(count: Int) {
        if (count > 0) {
            cvNewPostsCount.visibility = View.VISIBLE
            tvCount.text = "$count new posts"
        } else {
            cvNewPostsCount.visibility = View.GONE
        }
    }
}

