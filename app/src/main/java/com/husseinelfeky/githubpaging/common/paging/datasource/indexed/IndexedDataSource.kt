package com.husseinelfeky.githubpaging.common.paging.datasource.indexed

import com.husseinelfeky.githubpaging.common.paging.datasource.base.BaseDataSource
import com.husseinelfeky.githubpaging.common.paging.datasource.common.CachingLayer
import com.husseinelfeky.githubpaging.common.paging.datasource.common.FetchingStrategy
import io.reactivex.Single
import timber.log.Timber

/**
 * @param Index Type of data used to query Entity types out of the [IndexedDataSource].
 * @param Entity Type of items being loaded by the [IndexedDataSource].
 */
abstract class IndexedDataSource<Index : Any, Entity : Any> : BaseDataSource() {

    private val pagedList = LinkedHashSet<Entity>()

    fun getPagedList(): LinkedHashSet<Entity> = pagedList

    fun getOffset(page: Int) = (page - 1) * getPageSize()

    fun fetchItems(
        item: Index,
        fetchingStrategy: FetchingStrategy = FetchingStrategy.NETWORK_FIRST,
        vararg params: Any
    ): Single<List<Entity>> {
        when (fetchingStrategy) {
            FetchingStrategy.CACHE_FIRST -> {
                return fetchItemsFromDatabase(item, *params)
                    .doOnError { throwable ->
                        Timber.e(throwable, "Failed to fetch items from cache.")
                    }
                    .flatMap {
                        if (it.isEmpty()) {
                            return@flatMap fetchAndSaveIfRequired(item, *params)
                        }
                        // If local database items exist, return them.
                        return@flatMap Single.just(it)
                    }
            }
            FetchingStrategy.NETWORK_FIRST -> {
                return fetchAndSaveIfRequired(item, *params)
                    .onErrorResumeNext {
                        return@onErrorResumeNext fetchItemsFromDatabase(item, *params)
                            .doOnError { throwable ->
                                Timber.e(throwable, "Failed to fetch items from cache.")
                            }
                    }
            }
            FetchingStrategy.CACHE_ONLY -> {
                return fetchItemsFromDatabase(item, *params)
                    .doOnError { throwable ->
                        Timber.e(throwable, "Failed to fetch items from cache.")
                    }
            }
            FetchingStrategy.NETWORK_ONLY -> {
                return fetchAndSaveIfRequired(item, *params)
            }
        }
    }

    private fun fetchAndSaveIfRequired(item: Index, vararg params: Any): Single<List<Entity>> {
        val items = fetchItemsFromNetwork(item, *params)
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

    abstract fun getPageSize(): Int

    protected abstract fun fetchItemsFromNetwork(
        item: Index,
        vararg params: Any
    ): Single<List<Entity>>

    protected abstract fun fetchItemsFromDatabase(
        item: Index,
        vararg params: Any
    ): Single<List<Entity>>
}
