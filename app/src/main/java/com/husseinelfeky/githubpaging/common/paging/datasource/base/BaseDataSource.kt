package com.husseinelfeky.githubpaging.common.paging.datasource.base

import io.reactivex.subjects.BehaviorSubject

abstract class BaseDataSource {

    private val totalPages = BehaviorSubject.createDefault(Int.MAX_VALUE)

    fun getTotalPages(): BehaviorSubject<Int> = totalPages
}
