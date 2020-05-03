package com.husseinelfeky.githubpaging.common.paging.state

sealed class NetworkState {

    object Loading : NetworkState()

    object Loaded : NetworkState()

    data class Error(val error: Throwable) : NetworkState() {
        override fun toString() = "Error: $error"
    }
}
