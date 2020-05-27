package com.husseinelfeky.githubpaging.paging.datasource.base

abstract class BaseNetworkResource<Entity> : PagingSource() {

    private val pagedList = LinkedHashSet<Entity>()

    protected fun getPagedList(): LinkedHashSet<Entity> = pagedList

    protected fun updateEndPosition(lastItem: Int) {
        getEndPosition().onNext(lastItem)
    }
}
