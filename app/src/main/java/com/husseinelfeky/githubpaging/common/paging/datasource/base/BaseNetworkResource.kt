package com.husseinelfeky.githubpaging.common.paging.datasource.base

abstract class BaseNetworkResource<Entity> : BaseDataSource() {

    private val networkPagedList = LinkedHashSet<Entity>()

    fun getPagedList(): LinkedHashSet<Entity> = networkPagedList

    abstract fun getPageSize(): Int
}
