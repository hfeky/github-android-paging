package com.husseinelfeky.githubpaging.repository.userwithrepos

import com.husseinelfeky.githubpaging.common.paging.datasource.common.FetchingStrategy
import com.husseinelfeky.githubpaging.persistence.entities.UserWithRepos
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber

class UserWithReposRepository(
    private val usersFetchingRepo: UsersFetchingRepo = UsersFetchingRepo(),
    private val gitHubReposFetchingRepo: GitHubReposFetchingRepo = GitHubReposFetchingRepo()
) {

    fun getTotalPages(): BehaviorSubject<Int> = usersFetchingRepo.getTotalPages()

    fun getUsersWithRepos(
        item: Long,
        fetchingStrategy: FetchingStrategy = FetchingStrategy.NETWORK_FIRST
    ): Flowable<List<UserWithRepos>> {
        return usersFetchingRepo.fetchItems(item)
            .doOnSuccess {
                Timber.i("Fetched ${it.size} users")
            }
            .doOnError {
                Timber.e(it)
            }
            .toObservable()
            .flatMap { users -> Observable.fromIterable(users) }
            .flatMap { user ->
                gitHubReposFetchingRepo.fetchItems(fetchingStrategy, user.userName, user.id)
                    .doOnSuccess {
                        Timber.i("Fetched ${it.size} repos of user id ${user.id}")
                    }
                    .doOnError {
                        Timber.e(it)
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .toObservable()
            }
            .doOnComplete {
                Timber.i("Fetching completed")
            }
            .doOnError {
                Timber.e(it)
            }
            // == toCompletable
            .ignoreElements()
            .andThen(
                UserWithReposDataSource.gitHubDao.getUsersWithRepos(
                    item + usersFetchingRepo.getPageSize()
                )
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}
