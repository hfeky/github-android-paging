package com.husseinelfeky.githubpaging.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import com.husseinelfeky.githubpaging.R
import com.husseinelfeky.githubpaging.common.NetworkState
import com.husseinelfeky.githubpaging.persistence.AppRoomDatabase.Companion.getDatabase
import com.husseinelfeky.githubpaging.persistence.entities.UserWithRepos
import com.husseinelfeky.githubpaging.repository.ReposRepository
import com.husseinelfeky.githubpaging.ui.viewmodel.ReposViewModel
import com.husseinelfeky.githubpaging.ui.viewmodel.ReposViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_loading.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: ReposViewModel

    private val usersAdapter = UsersAdapter()

    private var isInitialLoad = true
    private var isRefreshing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        showLoading()
        initAdapter()
        initObservables()
    }

    private fun initAdapter() {
        viewModel = ViewModelProvider(
            this,
            ReposViewModelFactory(ReposRepository(getDatabase(this).gitHubDao()))
        ).get(ReposViewModel::class.java)

        swipe_refresh.setOnRefreshListener {
            isRefreshing = true
            viewModel.invalidateDataSource()
        }

        recycler_view.adapter = usersAdapter
    }

    private fun initObservables() {
        // It only gets observed once on initial data load as it only observes
        // the object reference itself but not the content inside.
        viewModel.usersWithReposPagedList.observe(
            this,
            Observer<PagedList<UserWithRepos>> { usersWithRepos ->
                hideLoading()
                updateUsersWithReposList(usersWithRepos)
                usersAdapter.setNetworkState(NetworkState.LOADED)

                empty_layout.visibility = if (usersWithRepos.isEmpty()) {
                    View.VISIBLE
                } else {
                    View.GONE
                }

                Toast.makeText(
                    this@MainActivity,
                    "${usersWithRepos.size} users initially loaded",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )

        viewModel.networkState.observe(this, Observer {
            usersAdapter.setNetworkState(it)
        })

        viewModel.refreshState.observe(this, Observer {
            swipe_refresh.isRefreshing = it == NetworkState.LOADING
        })
    }

    private fun updateUsersWithReposList(usersWithRepos: PagedList<UserWithRepos>) {
        usersAdapter.submitList(usersWithRepos) {
            if (isRefreshing) {
                recycler_view.scrollToPosition(0)
                isRefreshing = false
            }
        }
    }

    private fun showLoading() {
        if (isInitialLoad) {
            recycler_view.visibility = View.GONE
            progress_bar.visibility = View.VISIBLE
        }
    }

    private fun hideLoading() {
        if (isInitialLoad) {
            progress_bar.visibility = View.GONE
            recycler_view.visibility = View.VISIBLE
            isInitialLoad = false
        }
        viewModel.refreshState.postValue(NetworkState.LOADED)
    }
}
