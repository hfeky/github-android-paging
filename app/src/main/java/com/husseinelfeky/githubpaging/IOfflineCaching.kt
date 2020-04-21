package com.husseinelfeky.githubpaging

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface IOfflineCaching<Entity> {

    fun fetchItems(forceNetwork: Boolean = false): Single<List<Entity>> {
        return fetchItems(forceNetwork = forceNetwork, page = 0)
    }

    fun fetchItems(forceNetwork: Boolean = false, page: Int): Single<List<Entity>> {
        var fetchedList: List<Entity> = listOf()
        if (!forceNetwork) {
            return fetchItemsFromDB(page)
                    .flatMap {
                        if (it.isEmpty()) {
                            val emptyDataError = Exception("Empty Table")
                            return@flatMap Single.error(emptyDataError)
                        }
                        return@flatMap Single.just(it)
                    }
                    .doOnError { this.fetchItemsFromNetwork(page) }
        }
        return fetchItemsFromNetwork(page)
                .flatMapCompletable {
                    fetchedList = it
                    return@flatMapCompletable this.saveItemsToLocalDB(it)
                }
                .andThen(Single.just(fetchedList))
    }

    fun fetchItemsFromNetwork(page: Int): Single<List<Entity>>

    fun fetchItemsFromDB(page: Int): Single<List<Entity>>

    fun saveItemsToLocalDB(itemsList: List<Entity>): Completable

    fun deleteAllCachedItems(): Completable
}