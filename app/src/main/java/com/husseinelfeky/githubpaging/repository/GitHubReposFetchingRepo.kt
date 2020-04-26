package com.husseinelfeky.githubpaging.repository

import com.husseinelfeky.githubpaging.IOfflineCaching
import com.husseinelfeky.githubpaging.api.GitHubApi
import com.husseinelfeky.githubpaging.api.RetrofitClient
import com.husseinelfeky.githubpaging.persistence.AppRoomDatabase
import com.husseinelfeky.githubpaging.persistence.entities.UserWithRepos
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class GitHubReposFetchingRepo: IOfflineCaching<UserWithRepos> {

    private val gitHubAPI = RetrofitClient.getClient().create(GitHubApi::class.java)
    private val db = AppRoomDatabase.getDatabase().gitHubDao()

    override fun fetchItemsFromNetwork(page: Int): Single<List<UserWithRepos>> {
        TODO("Not yet implemented")
    }

    override fun fetchItemsFromDB(page: Int): Single<List<UserWithRepos>> {
        return db.getUsersWithReposRx()
    }

    override fun saveItemsToLocalDB(itemsList: List<UserWithRepos>): Completable {
        TODO("Not yet implemented")
    }

    override fun deleteAllCachedItems(): Completable {
        return db.deleteAllRx()
    }
}