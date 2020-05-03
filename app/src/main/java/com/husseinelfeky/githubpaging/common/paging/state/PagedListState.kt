package com.husseinelfeky.githubpaging.common.paging.state

sealed class PagedListState {

    object Loading : PagedListState()

    object Loaded : PagedListState()

    object Empty : PagedListState()

    data class Error(val error: Throwable) : PagedListState() {
        override fun toString() = "Error: $error"
    }
}
