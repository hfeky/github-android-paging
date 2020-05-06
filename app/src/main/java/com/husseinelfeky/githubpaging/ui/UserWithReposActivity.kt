package com.husseinelfeky.githubpaging.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.MergeAdapter
import com.husseinelfeky.githubpaging.R
import com.husseinelfeky.githubpaging.common.paging.adapter.NetworkStateAdapter
import com.husseinelfeky.githubpaging.common.paging.base.ItemsLoadedCallback
import com.husseinelfeky.githubpaging.common.paging.base.PagingCallback
import com.husseinelfeky.githubpaging.common.paging.state.NetworkState
import com.husseinelfeky.githubpaging.common.paging.state.PagedListState
import com.husseinelfeky.githubpaging.common.paging.utils.setupPaging
import com.husseinelfeky.githubpaging.persistence.entities.UserWithRepos
import com.husseinelfeky.githubpaging.repository.userwithrepos.UserWithReposRepository
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_loading.*

class UserWithReposActivity : AppCompatActivity() {

    private lateinit var viewModel: UserWithReposViewModel

    private val onItemsLoadedCallback = object : ItemsLoadedCallback<UserWithRepos>() {
        override fun invoke(list: List<UserWithRepos>) {
            listAdapter.updateList(list)
            Toast.makeText(
                this@UserWithReposActivity,
                "${listAdapter.itemCount} items loaded",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private val listAdapter = UserWithReposAdapter()
    private val networkStateAdapter = NetworkStateAdapter {
        viewModel.retryFetchingNextPage(onItemsLoadedCallback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        initViewModel()
        initObservers()
        initAdapter()
        initListeners()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            UserWithReposViewModel.Factory(
                UserWithReposRepository()
            )
        ).get(UserWithReposViewModel::class.java)
    }

    private fun initObservers() {
        viewModel.pagedListState.observe(this, Observer {
            when (it) {
                is PagedListState.Loading -> {
                    hideAllPagedListStateViews()
                    progress_bar.visibility = View.VISIBLE
                }
                is PagedListState.Loaded -> {
                    hideAllPagedListStateViews()
                    recycler_view.visibility = View.VISIBLE
                }
                is PagedListState.Empty -> {
                    hideAllPagedListStateViews()
                    empty_layout.visibility = View.VISIBLE
                }
                is PagedListState.Error -> {
                    hideAllPagedListStateViews()
                    error_layout.visibility = View.VISIBLE
                }
            }
        })

        viewModel.networkState.observe(this, Observer {
            networkStateAdapter.networkState = it
        })

        viewModel.refreshState.observe(this, Observer {
            swipe_refresh.isRefreshing = it == NetworkState.Loading
        })
    }

    private fun initAdapter() {
        recycler_view.adapter = MergeAdapter(listAdapter, networkStateAdapter)

        recycler_view.setupPaging(object : PagingCallback {
            override fun onSetupFinish() {
                viewModel.fetchInitialPage(onItemsLoadedCallback)
            }

            override fun onLoadMoreItems() {
                viewModel.fetchNextPage(onItemsLoadedCallback)
            }
        })
    }

    private fun initListeners() {
        swipe_refresh.setOnRefreshListener {
            viewModel.invalidateDataSource(onItemsLoadedCallback)
        }
    }

    private fun hideAllPagedListStateViews() {
        error_layout.visibility = View.GONE
        empty_layout.visibility = View.GONE
        progress_bar.visibility = View.GONE
        recycler_view.visibility = View.GONE
    }
}
