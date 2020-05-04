package com.husseinelfeky.githubpaging.common.paging

import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.addOnBottomBoundaryReachedCallback(boundaryCallback: () -> Unit) {
    addOnBoundaryReachedCallback(boundaryCallback, 1)
}

fun RecyclerView.addOnTopBoundaryReachedCallback(boundaryCallback: () -> Unit) {
    addOnBoundaryReachedCallback(boundaryCallback, -1)
}

private fun RecyclerView.addOnBoundaryReachedCallback(
    boundaryCallback: () -> Unit,
    direction: Int
) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (!recyclerView.canScrollVertically(direction) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                boundaryCallback.invoke()
            }
        }
    })
}
