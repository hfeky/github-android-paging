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

    abstract fun fetchInitialItems(onItemsLoadedCallback: (items: List<Entity>) -> Unit)

    abstract fun fetchNextPage(onItemsLoadedCallback: (items: List<Entity>) -> Unit)

    abstract fun invalidateDataSource()

    open fun retryFetchingLastPage(onItemsLoadedCallback: (items: List<Entity>) -> Unit) {
        fetchNextPage { onItemsLoadedCallback(it) }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
