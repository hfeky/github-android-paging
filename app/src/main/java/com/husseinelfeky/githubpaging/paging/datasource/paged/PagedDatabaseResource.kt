package com.husseinelfeky.githubpaging.paging.datasource.paged

import com.husseinelfeky.githubpaging.paging.datasource.base.BaseDatabaseResource
import io.reactivex.Flowable

/**
 * @param Entity Type of items being loaded by the [PagedDatabaseResource].
 */
abstract class PagedDatabaseResource<Entity : Any> : BaseDatabaseResource() {

    protected abstract fun fetchItemsFromDatabaseUntil(
        page: Int,
        vararg params: Any?
    ): Flowable<List<Entity>>
}
