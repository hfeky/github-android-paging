package com.husseinelfeky.githubpaging.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.husseinelfeky.githubpaging.R
import com.husseinelfeky.githubpaging.repository.FetchingRepo
import com.husseinelfeky.githubpaging.ui.section.GitHubSectionedAdapter
import com.husseinelfeky.githubpaging.ui.viewmodel.ReposViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_loading.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: ReposViewModel

    // private val usersAdapter = UsersAdapter()
    private val sectionedAdapter = GitHubSectionedAdapter()
    private var isInitialLoad = true
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
//        val viewModelFactory = ReposViewModelFactory(ReposRepository(getDatabase(this).gitHubDao()))
//        viewModel = ViewModelProvider(this, viewModelFactory).get(ReposViewModel::class.java)
//        swipe_refresh.setOnRefreshListener {
//            isRefreshing = true
//            viewModel.invalidateDataSource()
//        }
        val fetchingRepo = FetchingRepo()

        recycler_view.adapter = sectionedAdapter

        // Add bottom boundary callback
//        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    if (!isLoading) {
//                        // Fetch next page if it is not already fetching
//                        fetchingRepo.getUsersWithRepos(currentPage++)
//                            .doOnSubscribe {
//                                isLoading = true
//                            }
//                            .doOnSuccess { usersWithRepos ->
//                                isLoading = false
//                                usersWithRepos.forEach {
//                                    sectionedAdapter.addSection(UserWithReposSection(it))
//                                }
//                            }
//                            .doOnError {
//                                isLoading = false
//                            }.subscribe()
//                    }
//                }
//            }
//        })

        // Fetch initial items
        fetchingRepo.getUsersWithRepos(1)
            .doOnSubscribe {
                isLoading = true
                showLoading()
            }
            .subscribe({
                isLoading = false
                hideLoading()
                it.forEach {
                    sectionedAdapter.addSection(UserWithReposSection(it))
                }
            }, {
                isLoading = false
                hideLoading()
                Log.e("FetchingError ", it.toString())
            })


//            .doOnSuccess { usersWithRepos ->
//                isLoading = false
//                hideLoading()
//                usersWithRepos.forEach {
//                    sectionedAdapter.addSection(UserWithReposSection(it))
//                }
//            }
//            .doOnError {
//                isLoading = false
//                hideLoading()
//            }.subscribe()
    }

    private fun initObservables() {
//        // It only gets observed once on initial data load as it only observes
//        // the object reference itself but not the content inside.
//        viewModel.usersWithReposPagedList.observe(
//            this,
//            Observer { usersWithRepos ->
//                hideLoading()
//                updateUsersWithReposList(usersWithRepos)
////                sectionedAdapter.setNetworkState(NetworkState.LOADED)
//
//                empty_layout.visibility = if (usersWithRepos.isEmpty()) {
//                    View.VISIBLE
//                } else {
//                    View.GONE
//                }
//
//                Toast.makeText(
//                    this@MainActivity,
//                    "${usersWithRepos.size} users initially loaded",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        )
//
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
//        viewModel.refreshState.postValue(NetworkState.LOADED)
    }
}
