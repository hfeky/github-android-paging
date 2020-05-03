package com.husseinelfeky.githubpaging.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.MergeAdapter
import com.husseinelfeky.githubpaging.R
import com.husseinelfeky.githubpaging.common.paging.NetworkStateAdapter
import com.husseinelfeky.githubpaging.common.paging.setOnBottomBoundaryReachedCallback
import com.husseinelfeky.githubpaging.common.paging.state.NetworkState
import com.husseinelfeky.githubpaging.common.paging.state.PagedListState
import com.husseinelfeky.githubpaging.repository.userwithrepos.UserWithReposRepository
import com.husseinelfeky.githubpaging.ui.adapter.UserWithReposAdapter
import com.husseinelfeky.githubpaging.ui.adapter.UserWithReposSection
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_loading.*
import timber.log.Timber

class UserWithReposActivity : AppCompatActivity() {

    private lateinit var viewModel: UserWithReposViewModel
    private val compositeDisposable = CompositeDisposable()

    private val sectionedAdapter = UserWithReposAdapter()
    private val networkStateAdapter = NetworkStateAdapter { viewModel.retry() }

    private var currentPage = 1
    private var isRefreshing = false

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

        // TODO: Implement refresh observer.
//        viewModel.refreshState.observe(this, Observer {
//            swipe_refresh.isRefreshing = it == NetworkState.LOADING
//        })
    }

    private fun initAdapter() {
        recycler_view.adapter = MergeAdapter(sectionedAdapter, networkStateAdapter)

        // Add bottom boundary callback.
        recycler_view.setOnBottomBoundaryReachedCallback {
            if (viewModel.networkState.value !is NetworkState.Loading) {
                // Fetch next page if it is not already fetching.
                // TODO: Move logic to ViewModel.
                compositeDisposable.add(
                    viewModel.getUsersWithRepos(currentPage++)
                        .doOnSubscribe {
                            viewModel.networkState.postValue(NetworkState.Loading)
                        }
                        .subscribe({ usersWithRepos ->
                            viewModel.networkState.postValue(NetworkState.Loaded)
                            usersWithRepos.forEach { userWithRepos ->
                                sectionedAdapter.addSection(
                                    UserWithReposSection(
                                        userWithRepos
                                    )
                                )
                            }
                        }, {
                            viewModel.networkState.postValue(NetworkState.Error(it))
                        })
                )
            }
        }

        // Fetch initial items.
        // TODO: Move logic to ViewModel.
        compositeDisposable.add(
            viewModel.getUsersWithRepos(1)
                .onBackpressureBuffer(1024)
                .doOnSubscribe {
                    viewModel.pagedListState.postValue(PagedListState.Loading)
                }
                .subscribe({ usersWithRepos ->
                    if (usersWithRepos.isEmpty()) {
                        viewModel.pagedListState.postValue(PagedListState.Empty)
                    } else {
                        viewModel.pagedListState.postValue(PagedListState.Loaded)
                        usersWithRepos.forEach { userWithRepos ->
                            sectionedAdapter.addSection(
                                UserWithReposSection(
                                    userWithRepos
                                )
                            )
                        }
                    }
                }, {
                    viewModel.pagedListState.postValue(PagedListState.Error(it))
                    Timber.e(it.toString())
                })
        )
    }

    private fun initListeners() {
        swipe_refresh.setOnRefreshListener {
            isRefreshing = true
            viewModel.invalidateDataSource()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    private fun hideAllPagedListStateViews() {
        error_layout.visibility = View.GONE
        empty_layout.visibility = View.GONE
        progress_bar.visibility = View.GONE
        recycler_view.visibility = View.GONE
    }
}
