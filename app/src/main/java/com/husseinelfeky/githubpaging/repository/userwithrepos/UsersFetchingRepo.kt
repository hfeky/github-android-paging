package com.husseinelfeky.githubpaging.repository.userwithrepos

import com.husseinelfeky.githubpaging.common.paging.datasource.common.CachingLayer
import com.husseinelfeky.githubpaging.common.paging.datasource.indexed.IndexedDataSource
import com.husseinelfeky.githubpaging.persistence.entities.User
import io.reactivex.Completable

class UsersFetchingRepo : IndexedDataSource<Long, User>(), CachingLayer {

    private val api = UserWithReposDataSource.gitHubApi

    private val dao = UserWithReposDataSource.gitHubDao

    override fun getPageSize(): Int = 2

    override fun fetchItemsFromNetwork(item: Long, vararg params: Any) =
        api.getUsers(item, getPageSize())

    override fun fetchItemsFromDatabase(item: Long, vararg params: Any) =
        dao.getUsers(getPageSize(), item)

    override fun saveItemsToDatabase(itemsList: List<Any>): Completable =
        dao.insertUsers(itemsList as List<User>)
}
