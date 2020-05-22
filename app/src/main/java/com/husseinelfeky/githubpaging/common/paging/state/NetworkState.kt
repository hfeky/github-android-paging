package com.husseinelfeky.githubpaging.common.paging.state

import androidx.annotation.StringRes

sealed class NetworkState {

    object Loading : NetworkState()

    object Loaded : NetworkState()

    data class Error(
        val error: Throwable? = null,
        @StringRes val messageRes: Int? = null
    ) : NetworkState() {
        override fun toString(): String {
            return error?.localizedMessage ?: "Unknown Error"
        }
    }
}
