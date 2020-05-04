package com.husseinelfeky.githubpaging.common.paging.caching

import io.reactivex.Completable

interface IBaseCaching<Entity> {

    fun saveItemsToLocalDB(itemsList: List<Entity>): Completable

    fun deleteAllCachedItems(): Completable
}
