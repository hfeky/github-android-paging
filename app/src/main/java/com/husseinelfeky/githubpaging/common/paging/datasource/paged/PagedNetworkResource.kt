package com.husseinelfeky.githubpaging.common.paging.datasource.paged

import com.husseinelfeky.githubpaging.common.paging.datasource.base.BaseNetworkResource
import com.husseinelfeky.githubpaging.common.paging.datasource.common.CachingLayer
import io.reactivex.Single
import timber.log.Timber

/**
 * @param Entity Type of items being loaded by the [PagedNetworkResource].
 */
abstract class PagedNetworkResource<Entity : Any> : BaseNetworkResource<Entity>() {

    fun fetchAndSaveIfRequired(page: Int, vararg params: Any): Single<List<Entity>> {
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

    protected abstract fun fetchItemsFromNetwork(
        page: Int,
        vararg params: Any
    ): Single<List<Entity>>
}
