package com.husseinelfeky.githubpaging.common.paging.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.husseinelfeky.githubpaging.common.paging.state.NetworkState
import com.husseinelfeky.githubpaging.common.paging.state.PagedListState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlin.properties.Delegates

abstract class PagingViewModel<Entity : Any> : ViewModel() {

    protected val compositeDisposable = CompositeDisposable()

    private var pagedListDisposable: Disposable? = null

    protected val _pagedListState = MutableLiveData<PagedListState>()
    val pagedListState: LiveData<PagedListState>
        get() = _pagedListState

    protected val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    protected val _refreshState = MutableLiveData<NetworkState>()
    val refreshState: LiveData<NetworkState>
        get() = _refreshState

    protected var currentKey: Int by Delegates.observable(0) { _, _, newValue ->
        hasMorePages = newValue < endPosition
    }
    protected var endPosition = Int.MAX_VALUE

    protected var hasMorePages = true

    protected abstract fun loadInitialPage(callback: ItemsLoadedCallback<Entity>): Disposable

    protected abstract fun loadNextPage(callback: ItemsLoadedCallback<Entity>): Disposable

    protected abstract fun invalidateDataSource(callback: ItemsLoadedCallback<Entity>): Disposable

    protected abstract fun observeEndPosition()

    fun retryFetchingNextPage(callback: ItemsLoadedCallback<Entity>) {
        fetchNextPage(callback, true)
    }

    fun fetchInitialPage(callback: ItemsLoadedCallback<Entity>) {
        pagedListDisposable?.dispose()
        pagedListDisposable = loadInitialPage(callback)
    }

    private fun fetchNextPageInternal(callback: ItemsLoadedCallback<Entity>) {
        pagedListDisposable?.dispose()
        pagedListDisposable = loadNextPage(callback)
    }

    fun refresh(callback: ItemsLoadedCallback<Entity>) {
        pagedListDisposable?.dispose()
        pagedListDisposable = invalidateDataSource(callback)
    }

    /**
     *  If it is a forced fetch, fetch next page directly. If not, then fetch next
     *  page if there are more pages to be loaded, but only if it is not already
     *  fetching or there isn't an error.
     *
     *  @param forceFetch Set to true in case of a retry callback, false otherwise.
     */
    fun fetchNextPage(
        callback: ItemsLoadedCallback<Entity>,
        forceFetch: Boolean = false
    ): Boolean {
        if (forceFetch || (hasMorePages && !(_networkState.value is NetworkState.Loading || _networkState.value is NetworkState.Error))) {
            fetchNextPageInternal(callback)
            return true
        }
        return false
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
        pagedListDisposable?.dispose()
    }
}
