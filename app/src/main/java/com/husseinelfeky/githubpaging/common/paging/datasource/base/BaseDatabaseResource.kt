package com.husseinelfeky.githubpaging.common.paging.datasource.base

abstract class BaseDatabaseResource : PagingSource() {

    fun getItemsLimit(page: Int) = page * getPageSize()

    fun updateTotalPages(page: Int, itemsSize: Int) {
        val itemsLimit = getItemsLimit(page)
        if (itemsSize < itemsLimit) {
            getTotalPages().onNext(page)
        } else if (itemsSize == itemsLimit) {
            getTotalPages().onNext(Int.MAX_VALUE)
        }
    }
}
