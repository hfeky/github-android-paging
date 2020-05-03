package com.husseinelfeky.githubpaging.common.utils

import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.setOnBottomBoundaryReachedCallback(boundaryCallback: () -> Unit) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                boundaryCallback.invoke()
            }
        }
    })
}
