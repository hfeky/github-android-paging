package com.husseinelfeky.githubpaging.common.paging.caching

import io.reactivex.Single
import timber.log.Timber

interface IPagedCaching<Entity> : IBaseCaching<Entity> {

    fun getPageSize(): Int

    fun fetchItemsFromNetwork(page: Int, vararg params: Any): Single<List<Entity>>

    fun fetchItemsFromDB(page: Int, vararg params: Any): Single<List<Entity>>

    fun fetchItems(
        page: Int,
        fetchingStrategy: FetchingStrategy = FetchingStrategy.CACHE_FIRST,
        vararg params: Any
    ): Single<List<Entity>> {
        when (fetchingStrategy) {
            FetchingStrategy.CACHE_FIRST -> {
                return fetchItemsFromDB(page, *params)
                    .doOnError { throwable ->
                        Timber.e(throwable, "Failed to fetch items from cache.")
                    }
                    .flatMap {
                        if (it.isEmpty()) {
                            return@flatMap fetchAndSave(page, *params)
                        }
                        // If local database items exist, return them.
                        return@flatMap Single.just(it)
                    }
            }
            FetchingStrategy.NETWORK_FIRST -> {
                return fetchAndSave(page, *params)
                    .onErrorResumeNext {
                        return@onErrorResumeNext fetchItemsFromDB(page, *params)
                            .doOnError { throwable ->
                                Timber.e(throwable, "Failed to fetch items from cache.")
                            }
                    }
            }
            FetchingStrategy.CACHE_ONLY -> {
                return fetchItemsFromDB(page, *params)
                    .doOnError { throwable ->
                        Timber.e(throwable, "Failed to fetch items from cache.")
                    }
            }
            FetchingStrategy.NETWORK_ONLY -> {
                return fetchAndSave(page, *params)
            }
        }
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
}
