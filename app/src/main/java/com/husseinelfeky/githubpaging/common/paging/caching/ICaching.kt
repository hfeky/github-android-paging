package com.husseinelfeky.githubpaging.common.paging.caching

import io.reactivex.Completable
import io.reactivex.Single
import timber.log.Timber

interface ICaching<Entity> {

    fun fetchItems(
        forceNetwork: Boolean = false,
        vararg params: Any
    ): Single<List<Entity>> {
        if (!forceNetwork) {
            return fetchItemsFromDB(*params)
                .flatMap {
                    if (it.isEmpty()) {
                        return@flatMap fetchAndSave(*params)
                    }
                    // If local database items exist, return them.
                    return@flatMap Single.just(it)
                }
        }
        return fetchAndSave(*params)
    }

    private fun fetchAndSave(vararg params: Any): Single<List<Entity>> {
        return fetchItemsFromNetwork(*params)
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

    fun fetchItemsFromNetwork(vararg params: Any): Single<List<Entity>>

    fun fetchItemsFromDB(vararg params: Any): Single<List<Entity>>

    fun saveItemsToLocalDB(itemsList: List<Entity>): Completable

    fun deleteAllCachedItems(): Completable
}
