package com.husseinelfeky.githubpaging.repository.userwithrepos

import com.husseinelfeky.githubpaging.common.paging.caching.ICaching
import com.husseinelfeky.githubpaging.persistence.entities.GitHubRepo

class GitHubReposFetchingRepo : ICaching<GitHubRepo> {

    private val db = UserWithReposDataSource.gitHubDao

    override fun fetchItemsFromNetwork(vararg params: Any) =
        UserWithReposDataSource.gitHubApi.getUserRepos(
            userName = params.first().toString()
        )

    override fun fetchItemsFromDB(vararg params: Any) = db.getUserRepos(params[1] as Long)

    override fun saveItemsToLocalDB(itemsList: List<GitHubRepo>) = db.insertRepos(itemsList)

    override fun deleteAllCachedItems() = db.deleteAllRepos()
}
