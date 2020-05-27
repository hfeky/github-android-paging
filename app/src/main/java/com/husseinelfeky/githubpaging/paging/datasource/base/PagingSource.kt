package com.husseinelfeky.githubpaging.paging.datasource.base

import io.reactivex.subjects.BehaviorSubject

abstract class PagingSource {

    private val endPosition = BehaviorSubject.createDefault(Int.MAX_VALUE)

    fun getEndPosition(): BehaviorSubject<Int> = endPosition

    abstract fun getPageSize(): Int
}
