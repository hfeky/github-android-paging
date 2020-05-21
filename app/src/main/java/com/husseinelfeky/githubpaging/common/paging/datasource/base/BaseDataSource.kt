package com.husseinelfeky.githubpaging.common.paging.datasource.base

abstract class BaseDataSource<Entity> : PagingSource() {

    private val pagedList = LinkedHashSet<Entity>()

    fun getPagedList(): LinkedHashSet<Entity> = pagedList
}
