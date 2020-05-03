package com.husseinelfeky.githubpaging.repository.userwithrepos

import com.husseinelfeky.githubpaging.persistence.entities.UserWithRepos
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class UserWithReposRepository(
    private val usersFetchingRepo: GitHubUsersFetchingRepo = GitHubUsersFetchingRepo(),
    private val gitHubReposFetchingRepo: GitHubReposFetchingRepo = GitHubReposFetchingRepo()
) {

    fun getUsersWithRepos(page: Int): Flowable<List<UserWithRepos>> {
        return usersFetchingRepo
            .fetchItems(page = page)
            .toObservable()
            .flatMapIterable {
                it.map { user ->
                    gitHubReposFetchingRepo.fetchItems(user.userName, page = 1).doOnSuccess {
                        Timber.i("Fetched ${it.size} user repos")
                    }
                }
            }
            .doOnComplete {
                Timber.i("Completed")
            }
            .doOnError {
                Timber.i("Error")
            }
            // == toCompletable
            .ignoreElements()
            .andThen(UserWithReposDataSource.gitHubDao.getUsersWithReposRx())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}
