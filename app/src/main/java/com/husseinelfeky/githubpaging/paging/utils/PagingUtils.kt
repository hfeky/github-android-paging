package com.husseinelfeky.githubpaging.paging.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.husseinelfeky.githubpaging.paging.adapter.PagingAdapter
import com.husseinelfeky.githubpaging.paging.base.PagingSetupCallback
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun RecyclerView.setupPaging(
    pagingAdapter: PagingAdapter<*>,
    pagingSetupCallback: PagingSetupCallback,
    prefetchDistance: Int = 20,
    pagingDirection: PagingDirection = PagingDirection.DOWN
) {
    if (layoutManager == null) {
        throw IllegalStateException("RecyclerView LayoutManager is not initialized yet.")
    }

    if (layoutManager !is LinearLayoutManager) {
        throw IllegalStateException("${layoutManager!!.javaClass.simpleName} cannot be paginated. Use LinearLayoutManager or any other that extends it.")
    }

    val layoutManager = layoutManager as (LinearLayoutManager)

    val updateCallback = if (pagingDirection == PagingDirection.DOWN) {
        {
            if (layoutManager.itemCount - layoutManager.findLastVisibleItemPosition() <= prefetchDistance) {
                pagingSetupCallback.onLoadMoreItems()
            }
        }
    } else {
        {
            if (layoutManager.findFirstVisibleItemPosition() <= prefetchDistance) {
                pagingSetupCallback.onLoadMoreItems()
            }
        }
    }

    pagingAdapter.setOnListUpdatedCallback(updateCallback)

    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            updateCallback.invoke()
        }
    })

    pagingSetupCallback.onSetupFinish()
}

fun Disposable.addTo(compositeDisposable: CompositeDisposable): Disposable {
    compositeDisposable.add(this)
    return this
}
