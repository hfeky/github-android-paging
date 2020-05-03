package com.husseinelfeky.githubpaging.common.interfaces

import io.reactivex.Completable
import io.reactivex.Single
import timber.log.Timber

interface IOfflineCaching<Entity> {

    fun fetchItems(forceNetwork: Boolean = false, vararg params: String): Single<List<Entity>> {
        return fetchItems(forceNetwork = forceNetwork, page = 1)
    }

    fun fetchItems(
        vararg params: Any,
        forceNetwork: Boolean = false,
        page: Int
    ): Single<List<Entity>> {
        if (!forceNetwork) {
            return fetchItemsFromDB(*params, page = page)
                .flatMap {
                    if (it.isEmpty()) {
                        return@flatMap fetchAndSave(*params, page = page)
                    }
                    // If local database items exist, return them.
                    return@flatMap Single.just(it)
                }
        }
        return fetchAndSave(*params, page = page)
    }

    private fun fetchAndSave(vararg params: Any, page: Int): Single<List<Entity>> {
        return fetchItemsFromNetwork(*params, page = page)
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

    fun fetchItemsFromNetwork(vararg params: Any, page: Int): Single<List<Entity>>

    fun fetchItemsFromDB(vararg params: Any, page: Int): Single<List<Entity>>

    fun saveItemsToLocalDB(itemsList: List<Entity>): Completable

    fun deleteAllCachedItems(): Completable
}
