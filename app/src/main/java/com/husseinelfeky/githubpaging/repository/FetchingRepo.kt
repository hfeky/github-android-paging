package com.husseinelfeky.githubpaging.repository

import com.husseinelfeky.githubpaging.persistence.entities.UserWithRepos
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class FetchingRepo(
    private val usersFetchingRepo: GitHubUsersFetchingRepo = GitHubUsersFetchingRepo(),
    private val gitHubUsersReposFetchingRepo: GitHubUsersReposFetchingRepo = GitHubUsersReposFetchingRepo()
) {

    fun getUsersWithRepos(page: Int): Flowable<List<UserWithRepos>> {
        return usersFetchingRepo
            .fetchItems(page = page)
            .toObservable()
            .flatMapIterable {
                it.map { user ->
                    gitHubUsersReposFetchingRepo.fetchItems(user.userName, page = 1).doOnSuccess {
                        print("User Repos")
                    }
                }
            }
            .doOnComplete {
                print("Completed")
            }
            .doOnError {
                print("Error")
            }
            // == toCompletable
            .ignoreElements()
            .andThen(DataSource.db.getUsersWithReposRx())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}
