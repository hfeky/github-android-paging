package com.husseinelfeky.githubpaging.repository

import com.husseinelfeky.githubpaging.IOfflineCaching
import com.husseinelfeky.githubpaging.api.GitHubApi
import com.husseinelfeky.githubpaging.api.RetrofitClient
import com.husseinelfeky.githubpaging.models.responses.GitHubRepoResponse
import com.husseinelfeky.githubpaging.persistence.AppRoomDatabase
import com.husseinelfeky.githubpaging.persistence.entities.GitHubRepo
import com.husseinelfeky.githubpaging.persistence.entities.User
import com.husseinelfeky.githubpaging.persistence.entities.UserWithRepos
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.schedulers.Schedulers

class GitHubReposFetchingRepo : IOfflineCaching<UserWithRepos> {

    private val gitHubAPI = RetrofitClient.getClient().create(GitHubApi::class.java)
    private val db = AppRoomDatabase.getDatabase().gitHubDao()

    override fun fetchItemsFromNetwork(page: Int): Single<List<UserWithRepos>> {
        val reposLists = Observable.empty<List<List<GitHubRepoResponse>>>()

        val users = gitHubAPI.getUsersRx(page)
            .subscribeOn(Schedulers.io())
            .doOnSuccess { users ->
                users.forEach { user ->
                    reposLists.mergeWith(
                        gitHubAPI.getRepositoriesRx(user.userName)
                            .subscribeOn(Schedulers.io())
                            .toObservable()
                            .toList()
                    )
                }
            }

        return Single.zip(
            users,
            Single.fromObservable(reposLists),
            BiFunction { userResponses, reposResponses ->
                val result = mutableListOf<UserWithRepos>()

                for (i in userResponses.indices) {
                    val user = User(
                        userResponses[i].id,
                        userResponses[i].userName,
                        userResponses[i].avatarUrl
                    )
                    val repos = reposResponses[i].map { gitHubRepoResponse ->
                        GitHubRepo(
                            gitHubRepoResponse.id,
                            user.id,
                            gitHubRepoResponse.name
                        )
                    }
                    result.add(UserWithRepos(user, repos))
                }

                result
            }
        )
    }

    override fun fetchItemsFromDB(page: Int): Single<List<UserWithRepos>> {
        return db.getUsersWithReposRx()
    }

    override fun saveItemsToLocalDB(itemsList: List<UserWithRepos>): Completable {
        itemsList.forEach { userWithRepos ->
            with(userWithRepos) {
                db.insertUserRx(user)
                db.insertReposRx(repos)
            }
        }
        return Completable.complete()
    }

    override fun deleteAllCachedItems(): Completable {
        return db.deleteAllRx()
    }
}
