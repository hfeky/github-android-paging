package com.husseinelfeky.githubpaging.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.husseinelfeky.githubpaging.common.paging.base.ItemsLoadedCallback
import com.husseinelfeky.githubpaging.common.paging.base.PagingViewModel
import com.husseinelfeky.githubpaging.common.paging.state.NetworkState
import com.husseinelfeky.githubpaging.common.paging.state.PagedListState
import com.husseinelfeky.githubpaging.common.paging.utils.addTo
import com.husseinelfeky.githubpaging.persistence.entities.UserWithRepos
import com.husseinelfeky.githubpaging.repository.userwithrepos.UserWithReposRepository
import io.reactivex.disposables.Disposable
import timber.log.Timber

class UserWithReposViewModel(
    private val fetchingRepo: UserWithReposRepository
) : PagingViewModel<UserWithRepos>() {

    var currentItem = 0L

    init {
        observeTotalPages()
    }

    override fun loadInitialPage(callback: ItemsLoadedCallback<UserWithRepos>): Disposable =
        fetchingRepo.getUsersWithRepos(0)
            .doOnSubscribe {
                _pagedListState.postValue(PagedListState.Loading)
            }
            .subscribe({ usersWithRepos ->
                currentItem = usersWithRepos.last().user.id
                callback(usersWithRepos)
                if (usersWithRepos.isEmpty()) {
                    _pagedListState.postValue(PagedListState.Empty)
                } else {
                    _pagedListState.postValue(PagedListState.Loaded)
                }
            }, {
                Timber.e(it)
                _pagedListState.postValue(PagedListState.Error(it))
            })

    override fun loadNextPage(callback: ItemsLoadedCallback<UserWithRepos>): Disposable =
        fetchingRepo.getUsersWithRepos(currentItem)
            .doOnSubscribe {
                _networkState.postValue(NetworkState.Loading)
            }
            .subscribe({ usersWithRepos ->
                currentItem = usersWithRepos.last().user.id
                callback(usersWithRepos)
                _networkState.postValue(NetworkState.Loaded)
            }, {
                Timber.e(it)
                _networkState.postValue(NetworkState.Error(it))
            })

    override fun invalidateDataSource(callback: ItemsLoadedCallback<UserWithRepos>): Disposable =
        fetchingRepo.getUsersWithRepos(0)
            .doOnSubscribe {
                _refreshState.postValue(NetworkState.Loading)
            }
            .subscribe({ usersWithRepos ->
                currentItem = usersWithRepos.last().user.id
                callback(usersWithRepos)
                if (usersWithRepos.isEmpty()) {
                    _pagedListState.postValue(PagedListState.Empty)
                } else {
                    _pagedListState.postValue(PagedListState.Loaded)
                }
                _refreshState.postValue(NetworkState.Loaded)
            }, {
                Timber.e(it)
                _refreshState.postValue(NetworkState.Error(it))
            })

    override fun observeTotalPages() {
        fetchingRepo.getTotalPages()
            .subscribe { totalPages ->
                hasMorePages = currentPage < totalPages
            }
            .addTo(compositeDisposable)
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val fetchingRepo: UserWithReposRepository) : ViewModelProvider.Factory {
        override fun <VM : ViewModel?> create(modelClass: Class<VM>): VM =
            UserWithReposViewModel(fetchingRepo) as VM
    }
}
