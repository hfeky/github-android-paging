package com.husseinelfeky.githubpaging.common.paging.datasource.paged

import com.husseinelfeky.githubpaging.common.paging.datasource.base.BaseDataSource
import com.husseinelfeky.githubpaging.common.paging.datasource.common.CachingLayer
import com.husseinelfeky.githubpaging.common.paging.datasource.common.FetchingStrategy
import io.reactivex.Single
import timber.log.Timber

/**
 * @param Entity Type of items being loaded by the [PagedDataSource].
 */
abstract class PagedDataSource<Entity : Any> : BaseDataSource() {

    private val pagedList = LinkedHashSet<Entity>()

    fun getPagedList(): LinkedHashSet<Entity> = pagedList

    fun getOffset(page: Int) = (page - 1) * getPageSize()

    fun fetchItems(
        page: Int,
        fetchingStrategy: FetchingStrategy = FetchingStrategy.NETWORK_FIRST,
        vararg params: Any
    ): Single<List<Entity>> {
        when (fetchingStrategy) {
            FetchingStrategy.CACHE_FIRST -> {
                return fetchItemsFromDatabase(page, *params)
                    .doOnError { throwable ->
                        Timber.e(throwable, "Failed to fetch items from cache.")
                    }
                    .flatMap {
                        if (it.isEmpty()) {
                            return@flatMap fetchAndSaveIfRequired(page, *params)
                        }
                        // If local database items exist, return them.
                        return@flatMap Single.just(it)
                    }
            }
            FetchingStrategy.NETWORK_FIRST -> {
                return fetchAndSaveIfRequired(page, *params)
                    .onErrorResumeNext {
                        return@onErrorResumeNext fetchItemsFromDatabase(page, *params)
                            .doOnError { throwable ->
                                Timber.e(throwable, "Failed to fetch items from cache.")
                            }
                    }
            }
            FetchingStrategy.CACHE_ONLY -> {
                return fetchItemsFromDatabase(page, *params)
                    .doOnError { throwable ->
                        Timber.e(throwable, "Failed to fetch items from cache.")
                    }
            }
            FetchingStrategy.NETWORK_ONLY -> {
                return fetchAndSaveIfRequired(page, *params)
            }
        }
    }

    private fun fetchAndSaveIfRequired(page: Int, vararg params: Any): Single<List<Entity>> {
        val items = fetchItemsFromNetwork(page, *params)
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
        page: Int,
        vararg params: Any
    ): Single<List<Entity>>

    protected abstract fun fetchItemsFromDatabase(
        page: Int,
        vararg params: Any
    ): Single<List<Entity>>
}
