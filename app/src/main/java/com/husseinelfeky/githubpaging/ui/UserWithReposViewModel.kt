package com.husseinelfeky.githubpaging.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.husseinelfeky.githubpaging.common.paging.base.PagingViewModel
import com.husseinelfeky.githubpaging.common.paging.state.NetworkState
import com.husseinelfeky.githubpaging.common.paging.state.PagedListState
import com.husseinelfeky.githubpaging.common.utils.addToCompositeDisposable
import com.husseinelfeky.githubpaging.persistence.entities.UserWithRepos
import com.husseinelfeky.githubpaging.repository.userwithrepos.UserWithReposRepository
import timber.log.Timber

class UserWithReposViewModel(
    private val fetchingRepo: UserWithReposRepository
) : PagingViewModel<UserWithRepos>() {

    override fun fetchInitialItems(onItemsLoadedCallback: (items: List<UserWithRepos>) -> Unit) {
        fetchingRepo.getUsersWithRepos(1)
            .onBackpressureBuffer(1024)
            .doOnSubscribe {
                _pagedListState.postValue(PagedListState.Loading)
            }
            .subscribe({ usersWithRepos ->
                currentPage++
                if (usersWithRepos.isEmpty()) {
                    _pagedListState.postValue(PagedListState.Empty)
                } else {
                    _pagedListState.postValue(PagedListState.Loaded)
                    onItemsLoadedCallback(usersWithRepos)
                }
            }, {
                _pagedListState.postValue(PagedListState.Error(it))
                Timber.e(it)
            })
            .addToCompositeDisposable(compositeDisposable)
    }

    override fun fetchNextPage(onItemsLoadedCallback: (items: List<UserWithRepos>) -> Unit) {
        // Fetch next page if it is not already fetching.
        if (_networkState.value !is NetworkState.Loading) {
            fetchingRepo.getUsersWithRepos(currentPage)
                .doOnSubscribe {
                    _networkState.postValue(NetworkState.Loading)
                }
                .subscribe({ usersWithRepos ->
                    currentPage++
                    _networkState.postValue(NetworkState.Loaded)
                    onItemsLoadedCallback(usersWithRepos)
                }, {
                    _networkState.postValue(NetworkState.Error(it))
                    Timber.e(it)
                })
                .addToCompositeDisposable(compositeDisposable)
        }
    }

    override fun invalidateDataSource() {
        // TODO: Implement refresh logic.
        _refreshState.postValue(NetworkState.Loading)
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val fetchingRepo: UserWithReposRepository) : ViewModelProvider.Factory {
        override fun <VM : ViewModel?> create(modelClass: Class<VM>): VM =
            UserWithReposViewModel(fetchingRepo) as VM
    }
}
