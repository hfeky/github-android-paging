package com.husseinelfeky.githubpaging.repository.userwithrepos

import com.husseinelfeky.githubpaging.common.paging.caching.IPagedCaching
import com.husseinelfeky.githubpaging.persistence.entities.User

class UsersFetchingRepo : IPagedCaching<User> {

    private val db = UserWithReposDataSource.gitHubDao

    override fun getPageSize(): Int = 2

    /**
     * Set page size to "actualPageSize * pageToFetch" to support pagination by page.
     */
    override fun fetchItemsFromNetwork(page: Int, vararg params: Any) =
        UserWithReposDataSource.gitHubApi.getUsersRx(page, getPageSize() * page)

    override fun fetchItemsFromDB(page: Int, vararg params: Any) =
        db.getUsersRx(getPageSize(), (page - 1) * getPageSize())

    override fun saveItemsToLocalDB(itemsList: List<User>) = db.insertUsersRx(itemsList)

    override fun deleteAllCachedItems() = db.deleteAllUsersRx()
}
