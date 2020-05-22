package com.husseinelfeky.githubpaging.common.paging.datasource.base

abstract class BaseDatabaseResource : PagingSource() {

    protected fun getItemsLimit(page: Int) = page * getPageSize()

    protected fun updateEndPosition(currentPage: Int, itemsSize: Int) {
        val itemsLimit = getItemsLimit(currentPage)
        if (itemsSize < itemsLimit) {
            getEndPosition().onNext(currentPage + 1)
        } else if (itemsSize == itemsLimit) {
            getEndPosition().onNext(currentPage + 2)
        }
    }
}
