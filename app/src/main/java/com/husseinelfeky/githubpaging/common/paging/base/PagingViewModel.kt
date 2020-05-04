package com.husseinelfeky.githubpaging.common.paging.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.husseinelfeky.githubpaging.common.paging.state.NetworkState
import com.husseinelfeky.githubpaging.common.paging.state.PagedListState
import io.reactivex.disposables.CompositeDisposable

abstract class PagingViewModel<Entity> : ViewModel() {

    protected val compositeDisposable = CompositeDisposable()

    protected val _pagedListState = MutableLiveData<PagedListState>()
    val pagedListState: LiveData<PagedListState>
        get() = _pagedListState

    protected val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    protected val _refreshState = MutableLiveData<NetworkState>()
    val refreshState: LiveData<NetworkState>
        get() = _refreshState

    var currentPage = 1

    abstract fun fetchInitialPage(onItemsLoadedCallback: ItemsLoadedCallback<Entity>)

    abstract fun fetchNextPage(onItemsLoadedCallback: ItemsLoadedCallback<Entity>)

    abstract fun invalidateDataSource()

    open fun retryFetchingNextPage(onItemsLoadedCallback: ItemsLoadedCallback<Entity>) {
        fetchNextPageIfPossible(onItemsLoadedCallback, true)
    }

    /**
     *  Do not fetch next page if it is already fetching or there is an error
     *  unless it is a forced fetch.
     */
    fun fetchNextPageIfPossible(
        onItemsLoadedCallback: ItemsLoadedCallback<Entity>,
        forceFetch: Boolean = false
    ): Boolean {
        if (forceFetch || !(_networkState.value is NetworkState.Loading || _networkState.value is NetworkState.Error)) {
            fetchNextPage(onItemsLoadedCallback)
            return true
        }
        return false
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
