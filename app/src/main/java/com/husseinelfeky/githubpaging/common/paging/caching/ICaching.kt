package com.husseinelfeky.githubpaging.common.paging.caching

import io.reactivex.Single
import timber.log.Timber

interface ICaching<Entity> : IBaseCaching<Entity> {

    fun fetchItemsFromNetwork(vararg params: Any): Single<List<Entity>>

    fun fetchItemsFromDB(vararg params: Any): Single<List<Entity>>

    fun fetchItems(
        fetchingStrategy: FetchingStrategy = FetchingStrategy.CACHE_FIRST,
        vararg params: Any
    ): Single<List<Entity>> {
        when (fetchingStrategy) {
            FetchingStrategy.CACHE_FIRST -> {
                return fetchItemsFromDB(*params)
                    .doOnError { throwable ->
                        Timber.e(throwable, "Failed to fetch items from cache.")
                    }
                    .flatMap {
                        if (it.isEmpty()) {
                            return@flatMap fetchAndSave(*params)
                        }
                        // If local database items exist, return them.
                        return@flatMap Single.just(it)
                    }
            }
            FetchingStrategy.NETWORK_FIRST -> {
                return fetchAndSave(*params)
                    .onErrorResumeNext {
                        return@onErrorResumeNext fetchItemsFromDB(*params)
                            .doOnError { throwable ->
                                Timber.e(throwable, "Failed to fetch items from cache.")
                            }
                    }
            }
            FetchingStrategy.CACHE_ONLY -> {
                return fetchItemsFromDB(*params)
                    .doOnError { throwable ->
                        Timber.e(throwable, "Failed to fetch items from cache.")
                    }
            }
            FetchingStrategy.NETWORK_ONLY -> {
                return fetchAndSave(*params)
            }
        }
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
}
