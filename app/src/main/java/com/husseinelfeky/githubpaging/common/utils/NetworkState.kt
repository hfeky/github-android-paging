package com.husseinelfeky.githubpaging.common.utils

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED
}

data class NetworkState(
    val status: Status,
    val msg: String? = null
) {
    companion object {
        val LOADING = NetworkState(Status.RUNNING)
        val LOADED = NetworkState(Status.SUCCESS)
        fun error(msg: String?) = NetworkState(Status.FAILED, msg)
    }
}
