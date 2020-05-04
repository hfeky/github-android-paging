package com.husseinelfeky.githubpaging.common.paging.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.husseinelfeky.githubpaging.common.paging.base.PagingCallback

fun RecyclerView.setupPaging(
    pagingCallback: PagingCallback,
    prefetchDistance: Int = 10,
    pagingDirection: PagingDirection = PagingDirection.DOWN
) {
    if (layoutManager == null) {
        throw IllegalStateException("RecyclerView LayoutManager is not initialized yet.")
    }

    if (layoutManager !is LinearLayoutManager) {
        throw IllegalStateException("${layoutManager!!.javaClass.simpleName} cannot be paginated. Use LinearLayoutManager or any other that extends it.")
    }

    val layoutManager = layoutManager as (LinearLayoutManager)
    if (pagingDirection == PagingDirection.DOWN) {
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (layoutManager.itemCount - layoutManager.findLastVisibleItemPosition() <= prefetchDistance) {
                    pagingCallback.fetchNextPage()
                }
            }
        })
    } else {
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (layoutManager.findFirstVisibleItemPosition() <= prefetchDistance) {
                    pagingCallback.fetchNextPage()
                }
            }
        })
    }

    pagingCallback.fetchInitialPage()
}
