package com.husseinelfeky.githubpaging.common.paging.datasource.normal

import com.husseinelfeky.githubpaging.common.paging.datasource.common.CachingLayer
import com.husseinelfeky.githubpaging.common.paging.datasource.common.FetchingStrategy
import io.reactivex.Single
import timber.log.Timber

/**
 * @param Entity Type of items being loaded by the [NormalDataSource].
 */
abstract class NormalDataSource<Entity : Any> {

    open fun shouldFetchFromNetwork(databaseItems: List<Entity>): Boolean {
        return databaseItems.isEmpty()
    }

    fun fetchItems(
        fetchingStrategy: FetchingStrategy = FetchingStrategy.NETWORK_FIRST,
        vararg params: Any
    ): Single<List<Entity>> {
        when (fetchingStrategy) {
            FetchingStrategy.CACHE_FIRST -> {
                return fetchItemsFromDatabase(*params)
                    .doOnError { throwable ->
                        Timber.e(throwable, "Failed to fetch items from cache.")
                    }
                    .flatMap {
                        if (shouldFetchFromNetwork(it)) {
                            return@flatMap fetchAndSaveIfRequired(*params)
                        }
                        // If local database items exist, return them.
                        return@flatMap Single.just(it)
                    }
            }
            FetchingStrategy.NETWORK_FIRST -> {
                return fetchAndSaveIfRequired(*params)
                    .onErrorResumeNext {
                        return@onErrorResumeNext fetchItemsFromDatabase(*params)
                            .doOnError { throwable ->
                                Timber.e(throwable, "Failed to fetch items from cache.")
                            }
                    }
            }
            FetchingStrategy.CACHE_ONLY -> {
                return fetchItemsFromDatabase(*params)
                    .doOnError { throwable ->
                        Timber.e(throwable, "Failed to fetch items from cache.")
                    }
            }
            FetchingStrategy.NETWORK_ONLY -> {
                return fetchAndSaveIfRequired(*params)
            }
        }
    }

    private fun fetchAndSaveIfRequired(vararg params: Any): Single<List<Entity>> {
        val items = fetchItemsFromNetwork(*params)
            .doOnError { throwable ->
                Timber.e(throwable, "Failed to fetch items from network.")
            }
        return if (this is CachingLayer) {
            items.flatMap { list ->
                saveItemsToDatabase(list)
                    .doOnError { throwable ->
                        Timber.e(throwable, "Failed to save items to database.")
                    }
                    .andThen(Single.just(list))
            }
        } else {
            items
        }
    }

    protected abstract fun fetchItemsFromNetwork(vararg params: Any): Single<List<Entity>>

    protected abstract fun fetchItemsFromDatabase(vararg params: Any): Single<List<Entity>>
}
