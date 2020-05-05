package com.husseinelfeky.githubpaging.repository.userwithrepos

import com.husseinelfeky.githubpaging.common.paging.caching.FetchingStrategy
import com.husseinelfeky.githubpaging.persistence.entities.UserWithRepos
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class UserWithReposRepository(
    private val usersFetchingRepo: UsersFetchingRepo = UsersFetchingRepo(),
    private val gitHubReposFetchingRepo: GitHubReposFetchingRepo = GitHubReposFetchingRepo()
) {

    fun getUsersWithRepos(
        page: Int,
        fetchingStrategy: FetchingStrategy = FetchingStrategy.NETWORK_FIRST
    ): Flowable<List<UserWithRepos>> {
        return usersFetchingRepo.fetchItems(page)
            .doOnSuccess {
                Timber.i("Fetched ${it.size} user repos")
            }
            .doOnError {
                Timber.e(it)
            }
            .toObservable()
            .flatMapIterable { users ->
                users.map { user ->
                    gitHubReposFetchingRepo.fetchItems(fetchingStrategy, user.userName, user.id)
                        .doOnSuccess {
                            Timber.i("Fetched ${it.size} users")
                        }
                        .doOnError {
                            Timber.e(it)
                        }
                        .subscribeOn(Schedulers.io())
                        .subscribe()
                }
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
                    usersFetchingRepo.getPageSize() * page
                )
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}
