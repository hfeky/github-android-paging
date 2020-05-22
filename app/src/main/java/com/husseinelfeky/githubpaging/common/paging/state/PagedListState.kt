package com.husseinelfeky.githubpaging.common.paging.state

import androidx.annotation.StringRes

sealed class PagedListState {

    object Loading : PagedListState()

    object Loaded : PagedListState()

    object Updating : PagedListState()

    object Empty : PagedListState()

    data class Error(
        val error: Throwable? = null,
        @StringRes val messageRes: Int? = null
    ) : PagedListState() {
        override fun toString(): String {
            return error?.localizedMessage ?: "Unknown Error"
        }
    }
}
