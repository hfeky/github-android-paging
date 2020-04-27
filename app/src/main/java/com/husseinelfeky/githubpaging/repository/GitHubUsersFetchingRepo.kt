package com.husseinelfeky.githubpaging.repository

import com.husseinelfeky.githubpaging.IOfflineCaching
import com.husseinelfeky.githubpaging.persistence.entities.User

class GitHubUsersFetchingRepo: IOfflineCaching<User> {

    private val db = DataSource.db

    override fun fetchItemsFromNetwork(vararg params: Any, page: Int) = DataSource.gitHubAPI.getUsersRx(page)

    override fun fetchItemsFromDB(vararg params: Any, page: Int) = db.getUsersRx()

    override fun saveItemsToLocalDB(itemsList: List<User>) = db.insertUsersRx(itemsList)

    override fun deleteAllCachedItems() = db.deleteAllUsersRx()
}