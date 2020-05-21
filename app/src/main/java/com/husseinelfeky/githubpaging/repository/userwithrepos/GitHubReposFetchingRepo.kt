package com.husseinelfeky.githubpaging.repository.userwithrepos

import com.husseinelfeky.githubpaging.common.paging.datasource.common.CachingLayer
import com.husseinelfeky.githubpaging.common.paging.datasource.normal.NormalDataSource
import com.husseinelfeky.githubpaging.persistence.entities.GitHubRepo
import io.reactivex.Completable

class GitHubReposFetchingRepo : NormalDataSource<GitHubRepo>(), CachingLayer {

    private val api = UserWithReposDataSource.gitHubApi

    private val dao = UserWithReposDataSource.gitHubDao

    override fun fetchItemsFromNetwork(vararg params: Any) =
        api.getUserRepos(
            userName = params.first().toString()
        )

    override fun fetchItemsFromDatabase(vararg params: Any) =
        dao.getUserRepos(params[1] as Long)

    override fun saveItemsToDatabase(itemsList: List<Any>): Completable =
        dao.insertRepos(itemsList as List<GitHubRepo>)
}
