package com.husseinelfeky.githubpaging.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.husseinelfeky.githubpaging.R
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
    private var isRefreshing = false

    private var currentPage = 1
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
//        showLoading()
        initAdapter()
//        initObservables()
    }

    private fun initAdapter() {
//        swipe_refresh.setOnRefreshListener {
//            isRefreshing = true
//            viewModel.invalidateDataSource()
//        }

        recycler_view.adapter = sectionedAdapter

        val fetchingRepo = UserWithReposRepository()

        // Add bottom boundary callback.
        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!isLoading) {
                        // Fetch next page if it is not already fetching.
                        compositeDisposable.add(
                            fetchingRepo.getUsersWithRepos(currentPage++)
                                .doOnSubscribe {
                                    isLoading = true
                                }
                                .subscribe({ usersWithRepos ->
                                    isLoading = false
                                    usersWithRepos.forEach {
                                        sectionedAdapter.addSection(
                                            UserWithReposSection(
                                                it
                                            )
                                        )
                                    }
                                }, {
                                    isLoading = false
                                })
                        )
                    }
                }
            }
        })

        // Fetch initial items
        compositeDisposable.add(
            fetchingRepo.getUsersWithRepos(1)
                .onBackpressureBuffer(1024)
                .doOnSubscribe {
                    isLoading = true
                    showLoading()
                }
                .subscribe({ usersWithRepos ->
                    isLoading = false
                    hideLoading()
                    usersWithRepos.forEach {
                        sectionedAdapter.addSection(
                            UserWithReposSection(
                                it
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

    private fun initObservables() {
//        viewModel.networkState.observe(this, Observer {
//            sectionedAdapter.setNetworkState(it)
//        })
//
//        viewModel.refreshState.observe(this, Observer {
//            swipe_refresh.isRefreshing = it == NetworkState.LOADING
//        })
    }

//    private fun updateUsersWithReposList(usersWithRepos: PagedList<UserWithRepos>) {
//        //
//        var section: UserWithReposSection? = null
//        sectionedAdapter.addSection(section)
//        //
//        sectionedAdapter.submitList(usersWithRepos) {
//            if (isRefreshing) {
//                recycler_view.scrollToPosition(0)
//                isRefreshing = false
//            }
//        }
//    }

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
