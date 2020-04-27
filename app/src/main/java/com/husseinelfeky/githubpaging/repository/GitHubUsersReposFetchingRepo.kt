package com.husseinelfeky.githubpaging.repository

import com.husseinelfeky.githubpaging.IOfflineCaching
import com.husseinelfeky.githubpaging.persistence.entities.GitHubRepo

class GitHubUsersReposFetchingRepo : IOfflineCaching<GitHubRepo> {

    private val db = DataSource.db

    override fun fetchItemsFromNetwork(vararg params: Any, page: Int) = DataSource.gitHubAPI.getGitHubReposRx(page = page, userName = params.first() as String)

    override fun fetchItemsFromDB(vararg params: Any, page: Int) = db.getReposRx()

    override fun saveItemsToLocalDB(itemsList: List<GitHubRepo>) = db.insertReposRx(itemsList)

    override fun deleteAllCachedItems() = db.deleteAllReposRx()
}