package com.husseinelfeky.githubpaging.repository

import com.husseinelfeky.githubpaging.database.entities.GitHubRepo
import com.husseinelfeky.githubpaging.paging.datasource.basic.BasicDataSource
import com.husseinelfeky.githubpaging.paging.datasource.common.CachingLayer
import io.reactivex.Completable

class GitHubReposFetchingRepo : BasicDataSource<GitHubRepo>(), CachingLayer {

    private val api = UserWithReposDataSource.gitHubApi

    private val dao = UserWithReposDataSource.gitHubDao

    override fun fetchItemsFromNetwork(vararg params: Any?) =
        api.getUserRepos(
            userName = params.first().toString()
        )

    override fun fetchItemsFromDatabase(vararg params: Any?) =
        dao.getUserRepos(params[1] as Int)

    override fun saveItemsToDatabase(itemsList: List<Any>): Completable =
        dao.insertRepos(itemsList as List<GitHubRepo>)
}
