package com.husseinelfeky.githubpaging.common.paging.datasource.indexed

import com.husseinelfeky.githubpaging.common.paging.datasource.base.BaseNetworkResource
import com.husseinelfeky.githubpaging.common.paging.datasource.common.CachingLayer
import io.reactivex.Single
import timber.log.Timber

/**
 * @param Index Type of data used to query Entity types out of the [IndexedNetworkResource].
 * @param Entity Type of items being loaded by the [IndexedNetworkResource].
 */
abstract class IndexedNetworkResource<Index : Any, Entity : Any> : BaseNetworkResource<Entity>() {

    fun fetchAndSaveIfRequired(item: Index, vararg params: Any): Single<List<Entity>> {
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

    protected abstract fun fetchItemsFromNetwork(
        page: Index,
        vararg params: Any
    ): Single<List<Entity>>
}
