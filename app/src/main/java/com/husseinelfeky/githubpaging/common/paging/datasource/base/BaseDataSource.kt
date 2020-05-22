package com.husseinelfeky.githubpaging.common.paging.datasource.base

abstract class BaseDataSource<Entity> : PagingSource() {

    private val pagedList = LinkedHashSet<Entity>()

    protected fun getPagedList(): LinkedHashSet<Entity> = pagedList

    protected open fun shouldFetchFromNetwork(databaseItems: List<Entity>): Boolean {
        return databaseItems.isEmpty()
    }
}
