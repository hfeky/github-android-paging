package com.husseinelfeky.githubpaging.common.paging.base

interface PagingCallback {

    fun fetchInitialPage()

    fun fetchNextPageIfPossible()
}
