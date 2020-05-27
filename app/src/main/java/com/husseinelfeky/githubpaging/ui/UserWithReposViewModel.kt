package com.husseinelfeky.githubpaging.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.husseinelfeky.githubpaging.R
import com.husseinelfeky.githubpaging.database.entities.UserWithRepos
import com.husseinelfeky.githubpaging.paging.base.ItemsLoadedCallback
import com.husseinelfeky.githubpaging.paging.base.PagingViewModel
import com.husseinelfeky.githubpaging.paging.state.NetworkState
import com.husseinelfeky.githubpaging.paging.state.PagedListState
import com.husseinelfeky.githubpaging.paging.utils.addTo
import com.husseinelfeky.githubpaging.repository.UserWithReposRepository
import io.reactivex.disposables.Disposable
import timber.log.Timber

class UserWithReposViewModel(
    private val fetchingRepo: UserWithReposRepository
) : PagingViewModel<UserWithRepos>() {

    init {
        observeEndPosition()
    }

    override fun loadInitialPage(callback: ItemsLoadedCallback<UserWithRepos>): Disposable =
        fetchingRepo.getUsersWithRepos(0)
            .doOnSubscribe {
                _pagedListState.postValue(PagedListState.Loading)
            }
            .subscribe({ usersWithRepos ->
                callback(usersWithRepos)
                if (usersWithRepos.isEmpty()) {
                    _pagedListState.postValue(PagedListState.Empty)
                } else {
                    currentKey = usersWithRepos.last().user.id
                    _pagedListState.postValue(PagedListState.Loaded)
                }
            }, {
                Timber.e(it)
                _pagedListState.postValue(PagedListState.Error(it))
            })

    override fun loadNextPage(callback: ItemsLoadedCallback<UserWithRepos>): Disposable =
        fetchingRepo.getUsersWithRepos(currentKey)
            .doOnSubscribe {
                _networkState.postValue(NetworkState.Loading)
            }
            .subscribe({ usersWithRepos ->
                callback(usersWithRepos)
                currentKey = usersWithRepos.last().user.id
                if (!hasMorePages) {
                    _networkState.postValue(
                        NetworkState.Error(
                            messageRes = R.string.failed_to_load_more_users
                        )
                    )
                } else {
                    _networkState.postValue(NetworkState.Loaded)
                }
            }, {
                Timber.e(it)
                _networkState.postValue(NetworkState.Error(it, R.string.failed_to_load_more_users))
            })

    override fun invalidateDataSource(callback: ItemsLoadedCallback<UserWithRepos>): Disposable =
        fetchingRepo.getUsersWithRepos(0)
            .doOnSubscribe {
                _refreshState.postValue(NetworkState.Loading)
            }
            .subscribe({ usersWithRepos ->
                callback(usersWithRepos)
                if (usersWithRepos.isEmpty()) {
                    _pagedListState.postValue(PagedListState.Empty)
                } else {
                    currentKey = usersWithRepos.last().user.id
                    _pagedListState.postValue(PagedListState.Loaded)
                }
                _refreshState.postValue(NetworkState.Loaded)
            }, {
                Timber.e(it)
                _refreshState.postValue(NetworkState.Error(it))
            })

    override fun observeEndPosition() {
        fetchingRepo.getEndPosition()
            .subscribe { endPosition ->
                this.endPosition = endPosition
            }
            .addTo(compositeDisposable)
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val fetchingRepo: UserWithReposRepository) : ViewModelProvider.Factory {
        override fun <VM : ViewModel?> create(modelClass: Class<VM>): VM =
            UserWithReposViewModel(fetchingRepo) as VM
    }
}
