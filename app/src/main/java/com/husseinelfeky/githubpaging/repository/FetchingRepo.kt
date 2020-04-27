package com.husseinelfeky.githubpaging.repository

import com.husseinelfeky.githubpaging.persistence.entities.UserWithRepos
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

class FetchingRepo(private val usersFetchingRepo: GitHubUsersFetchingRepo = GitHubUsersFetchingRepo(),
                   private val gitHubUsersReposFetchingRepo: GitHubUsersReposFetchingRepo = GitHubUsersReposFetchingRepo()) {

    fun getUsersWithRepo(page: Int): Single<List<UserWithRepos>> {
        return usersFetchingRepo
            .fetchItems(page = page)
            .toObservable()
            // Converts the list of users into an Observable which emits each user in the list
            .flatMap {
                return@flatMap Observable.fromIterable(it)
            }
            // Fetch and save repos of each user
            .flatMap {
                return@flatMap gitHubUsersReposFetchingRepo.fetchItems(it.userName, page = 1).toObservable()
            }
            // == toCompletable
            .ignoreElements()
            .andThen(DataSource.db.getUsersWithReposRx())
    }
}