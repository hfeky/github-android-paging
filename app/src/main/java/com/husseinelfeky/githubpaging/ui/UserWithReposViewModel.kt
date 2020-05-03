package com.husseinelfeky.githubpaging.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.husseinelfeky.githubpaging.common.paging.state.NetworkState
import com.husseinelfeky.githubpaging.common.paging.state.PagedListState
import com.husseinelfeky.githubpaging.persistence.entities.UserWithRepos
import com.husseinelfeky.githubpaging.repository.userwithrepos.UserWithReposRepository
import io.reactivex.Flowable

// TODO: Make all states return LiveData instead of MutableLiveData.
class UserWithReposViewModel(private val fetchingRepo: UserWithReposRepository) : ViewModel() {

    val pagedListState = MutableLiveData<PagedListState>()

    val networkState = MutableLiveData<NetworkState>()

    // TODO: Implement refresh observable.
    val refreshState = MutableLiveData<NetworkState>()

    fun getUsersWithRepos(page: Int): Flowable<List<UserWithRepos>> {
        return fetchingRepo.getUsersWithRepos(page)
    }

    fun retry() {
        // TODO: Implement retry logic.
    }

    fun invalidateDataSource() {
        // TODO: Implement refresh logic.
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val fetchingRepo: UserWithReposRepository) : ViewModelProvider.Factory {
        override fun <VM : ViewModel?> create(modelClass: Class<VM>): VM =
            UserWithReposViewModel(fetchingRepo) as VM
    }
}
