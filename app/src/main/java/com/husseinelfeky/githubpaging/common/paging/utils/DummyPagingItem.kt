package com.husseinelfeky.githubpaging.common.paging.utils

import com.husseinelfeky.githubpaging.common.paging.base.PagingItem

data class DummyPagingItem(
    val string: String = ""
) : PagingItem {

    override fun getUniqueIdentifier(): Any = string

    override fun getContent(): String = toString()
}
