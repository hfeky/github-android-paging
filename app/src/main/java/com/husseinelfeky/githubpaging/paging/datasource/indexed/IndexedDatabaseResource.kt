package com.husseinelfeky.githubpaging.paging.datasource.indexed

import com.husseinelfeky.githubpaging.paging.datasource.base.BaseDatabaseResource
import io.reactivex.Flowable

/**
 * @param Index Type of data used to query Entity types out of the [IndexedDatabaseResource].
 * @param Entity Type of items being loaded by the [IndexedDatabaseResource].
 */
abstract class IndexedDatabaseResource<Index : Any, Entity : Any> : BaseDatabaseResource() {

    protected abstract fun fetchItemsFromDatabaseUntil(
        index: Index,
        vararg params: Any?
    ): Flowable<List<Entity>>
}
