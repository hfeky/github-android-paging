package com.husseinelfeky.githubpaging.common.paging.caching

import io.reactivex.Completable
import io.reactivex.Single
import timber.log.Timber

interface IPagedCaching<Entity> {

    fun fetchItems(
        forceNetwork: Boolean = false,
        page: Int,
        vararg params: Any
    ): Single<List<Entity>> {
        if (!forceNetwork) {
            return fetchItemsFromDB(page, *params)
                .flatMap {
                    if (it.isEmpty()) {
                        return@flatMap fetchAndSave(page, *params)
                    }
                    // If local database items exist, return them.
                    return@flatMap Single.just(it)
                }
        }
        return fetchAndSave(page, *params)
    }

    private fun fetchAndSave(page: Int, vararg params: Any): Single<List<Entity>> {
        return fetchItemsFromNetwork(page, *params)
            .doOnError { throwable ->
                Timber.e(throwable, "Failed to fetch items from network.")
            }
            .flatMap { list ->
                saveItemsToLocalDB(list)
                    .doOnError { throwable ->
                        Timber.e(throwable, "Failed to save to local database.")
                    }
                    .andThen(Single.just(list))
            }
    }

    fun fetchItemsFromNetwork(page: Int, vararg params: Any): Single<List<Entity>>

    fun fetchItemsFromDB(page: Int, vararg params: Any): Single<List<Entity>>

    fun saveItemsToLocalDB(itemsList: List<Entity>): Completable

    fun deleteAllCachedItems(): Completable
}
