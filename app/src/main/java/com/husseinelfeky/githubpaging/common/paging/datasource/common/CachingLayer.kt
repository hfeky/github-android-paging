package com.husseinelfeky.githubpaging.common.paging.datasource.common

import io.reactivex.Completable

interface CachingLayer {
    fun saveItemsToDatabase(itemsList: List<Any>): Completable
}
