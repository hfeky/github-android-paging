package com.husseinelfeky.githubpaging.repository.userwithrepos

import com.husseinelfeky.githubpaging.common.paging.base.IOfflineCaching
import com.husseinelfeky.githubpaging.persistence.entities.User

class GitHubUsersFetchingRepo :
    IOfflineCaching<User> {

    private val db = UserWithReposDataSource.gitHubDao

    override fun fetchItemsFromNetwork(vararg params: Any, page: Int) =
        UserWithReposDataSource.gitHubApi.getUsersRx(page)

    override fun fetchItemsFromDB(vararg params: Any, page: Int) = db.getUsersRx()

    override fun saveItemsToLocalDB(itemsList: List<User>) = db.insertUsersRx(itemsList)

    override fun deleteAllCachedItems() = db.deleteAllUsersRx()
}
