package com.husseinelfeky.githubpaging

import io.reactivex.Completable
import io.reactivex.Single

interface IOfflineCaching<Entity> {

    fun fetchItems(forceNetwork: Boolean = false, vararg params: String): Single<List<Entity>> {
        return fetchItems(forceNetwork = forceNetwork, page = 0)
    }

    fun fetchItems(
        vararg params: Any,
        forceNetwork: Boolean = false,
        page: Int
    ): Single<List<Entity>> {
        var fetchedList: List<Entity> = listOf()
        if (!forceNetwork) {
            return fetchItemsFromDB(params, page = page)
                .flatMap {
                    if (it.isEmpty()) {
                      //  val emptyDataError = Exception("Empty Table")
                      //  return@flatMap Single.error<List<Entity>>(emptyDataError)
                        return@flatMap  this.fetchItemsFromNetwork(params, page = page)
                            .doOnError {
                                println("Error Here!")
                            }
                            .flatMapCompletable {
                                fetchedList = it
                                return@flatMapCompletable this.saveItemsToLocalDB(it)
                            }.andThen(Single.just(fetchedList))
                    }
                    return@flatMap Single.just(it)
                }
                // Fix this
//                .doOnError {
//                    this.fetchItemsFromNetwork(params, page = page)
//                        .doOnError {
//                            println("Hopa")
//                        }
//                        .flatMapCompletable {
//                            fetchedList = it
//                            return@flatMapCompletable this.saveItemsToLocalDB(it)
//                        }.andThen(Single.just(fetchedList))
//                }
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
