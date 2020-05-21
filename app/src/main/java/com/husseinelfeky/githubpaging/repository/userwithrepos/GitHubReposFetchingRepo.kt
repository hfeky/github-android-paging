package com.husseinelfeky.githubpaging.repository.userwithrepos

import com.husseinelfeky.githubpaging.common.paging.datasource.normal.NormalDataSource
import com.husseinelfeky.githubpaging.common.paging.datasource.common.CachingLayer
import com.husseinelfeky.githubpaging.persistence.entities.GitHubRepo
import io.reactivex.Completable

class GitHubReposFetchingRepo : NormalDataSource<GitHubRepo>(), CachingLayer {

    private val db = UserWithReposDataSource.gitHubDao

    override fun fetchItemsFromNetwork(vararg params: Any) =
        UserWithReposDataSource.gitHubApi.getUserRepos(
            userName = params.first().toString()
        )

    override fun fetchItemsFromDatabase(vararg params: Any) = db.getUserRepos(params[1] as Long)

    override fun saveItemsToDatabase(itemsList: List<Any>): Completable =
        db.insertRepos(itemsList as List<GitHubRepo>)
}
