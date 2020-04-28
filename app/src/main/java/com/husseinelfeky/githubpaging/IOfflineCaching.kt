package com.husseinelfeky.githubpaging

import android.annotation.SuppressLint
import android.util.Log
import io.reactivex.Completable
import io.reactivex.Single

interface IOfflineCaching<Entity> {

    fun fetchItems(forceNetwork: Boolean = false, vararg params: String): Single<List<Entity>> {
        return fetchItems(forceNetwork = forceNetwork, page = 0)
    }

    @SuppressLint("LogNotTimber")
    fun fetchItems(
        vararg params: Any,
        forceNetwork: Boolean = false,
        page: Int
    ): Single<List<Entity>> {
        if (!forceNetwork) {
            return this
                .fetchItemsFromDB(*params, page = page)
                .flatMap {
                    if (it.isEmpty()) {
                        return@flatMap this.fetchAndSave(*params, page = page)
                    }
                    // If local database items exists, return them.
                    return@flatMap Single.just(it)
                }
        }
        return this.fetchAndSave(*params, page = page)
    }

    fun fetchItemsFromNetwork(vararg params: Any, page: Int): Single<List<Entity>>

    fun fetchItemsFromDB(vararg params: Any, page: Int): Single<List<Entity>>

    fun saveItemsToLocalDB(itemsList: List<Entity>): Completable

    fun deleteAllCachedItems(): Completable

    @SuppressLint("LogNotTimber")
    private fun fetchAndSave(vararg params: Any, page: Int): Single<List<Entity>> {
        return this
            .fetchItemsFromNetwork(*params, page = page)
            .doOnError { throwable ->
                Log.e("IOfflineCaching", "Fetching from network error ", throwable)
            }
            .flatMap { list ->
                this.saveItemsToLocalDB(list)

                    .doOnError { throwable ->
                        Log.e("IOfflineCaching", "Saving to local database error ", throwable)
                    }
                    .andThen(Single.just(list))
            }
    }
}
