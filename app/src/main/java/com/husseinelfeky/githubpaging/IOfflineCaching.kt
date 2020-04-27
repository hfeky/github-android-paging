package com.husseinelfeky.githubpaging

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface IOfflineCaching<Entity> {

    fun fetchItems(forceNetwork: Boolean = false, vararg params: String): Single<List<Entity>> {
        return fetchItems(forceNetwork = forceNetwork, page = 0)
    }

    fun fetchItems(vararg params: Any, forceNetwork: Boolean = false, page: Int): Single<List<Entity>> {
        var fetchedList: List<Entity> = listOf()
        if (!forceNetwork) {
            return fetchItemsFromDB(params, page = page)
                    .flatMap {
                        if (it.isEmpty()) {
                            val emptyDataError = Exception("Empty Table")
                            return@flatMap Single.error(emptyDataError)
                        }
                        return@flatMap Single.just(it)
                    }
                    .doOnError { this.fetchItemsFromNetwork(params, page = page) }
        }
        return fetchItemsFromNetwork(params, page = page)
                .flatMapCompletable {
                    fetchedList = it
                    return@flatMapCompletable this.saveItemsToLocalDB(it)
                }
                .andThen(Single.just(fetchedList))
    }

    fun fetchItemsFromNetwork(vararg params: Any, page: Int): Single<List<Entity>>

    fun fetchItemsFromDB(vararg params: Any, page: Int): Single<List<Entity>>

    fun saveItemsToLocalDB(itemsList: List<Entity>): Completable

    fun deleteAllCachedItems(): Completable
}