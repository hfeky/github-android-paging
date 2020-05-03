package com.husseinelfeky.githubpaging.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.husseinelfeky.githubpaging.R
import com.husseinelfeky.githubpaging.common.utils.setOnBottomBoundaryReachedCallback
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

    private var currentPage = 1
    private var isLoading = false
    private var isRefreshing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        initViewModel()
        initAdapter()
        initObservers()
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

    // TODO: Create MergeAdapter to show list bottom progress bar.
    private fun initAdapter() {
        recycler_view.adapter = sectionedAdapter

        // Add bottom boundary callback.
        recycler_view.setOnBottomBoundaryReachedCallback {
            if (!isLoading) {
                // Fetch next page if it is not already fetching.
                compositeDisposable.add(
                    viewModel.getUsersWithRepos(currentPage++)
                        .doOnSubscribe {
                            isLoading = true
                        }
                        .subscribe({ usersWithRepos ->
                            isLoading = false
                            usersWithRepos.forEach { userWithRepos ->
                                sectionedAdapter.addSection(
                                    UserWithReposSection(
                                        userWithRepos
                                    )
                                )
                            }
                        }, {
                            isLoading = false
                        })
                )
            }
        }

        // Fetch initial items
        compositeDisposable.add(
            viewModel.getUsersWithRepos(1)
                .onBackpressureBuffer(1024)
                .doOnSubscribe {
                    isLoading = true
                    showLoading()
                }
                .subscribe({ usersWithRepos ->
                    isLoading = false
                    hideLoading()
                    usersWithRepos.forEach { userWithRepos ->
                        sectionedAdapter.addSection(
                            UserWithReposSection(
                                userWithRepos
                            )
                        )
                    }
                }, {
                    isLoading = false
                    hideLoading()
                    Timber.e(it.toString())
                })
        )
    }

    // TODO: Implement loading/refresh observables.
    private fun initObservers() {
//        viewModel.networkState.observe(this, Observer {
//            sectionedAdapter.setNetworkState(it)
//        })
//
//        viewModel.refreshState.observe(this, Observer {
//            swipe_refresh.isRefreshing = it == NetworkState.LOADING
//        })
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

    private fun showLoading() {
        recycler_view.visibility = View.GONE
        progress_bar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        progress_bar.visibility = View.GONE
        recycler_view.visibility = View.VISIBLE
//        viewModel.refreshState.postValue(NetworkState.LOADED)
    }
}
