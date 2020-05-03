package com.husseinelfeky.githubpaging.repository.userwithrepos

import com.husseinelfeky.githubpaging.common.paging.base.IOfflineCaching
import com.husseinelfeky.githubpaging.persistence.entities.GitHubRepo

class GitHubReposFetchingRepo :
    IOfflineCaching<GitHubRepo> {

    private val db = UserWithReposDataSource.gitHubDao

    override fun fetchItemsFromNetwork(vararg params: Any, page: Int) =
        UserWithReposDataSource.gitHubApi.getAllRepositoriesRx(
            userName = params.first().toString()
        )

    override fun fetchItemsFromDB(vararg params: Any, page: Int) = db.getReposRx()

    override fun saveItemsToLocalDB(itemsList: List<GitHubRepo>) = db.insertReposRx(itemsList)

    override fun deleteAllCachedItems() = db.deleteAllReposRx()
}
