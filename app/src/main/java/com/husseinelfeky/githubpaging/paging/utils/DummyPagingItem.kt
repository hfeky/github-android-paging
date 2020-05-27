package com.husseinelfeky.githubpaging.paging.utils

import com.husseinelfeky.githubpaging.paging.base.PagingItem

data class DummyPagingItem(
    val string: String = ""
) : PagingItem {

    override fun getUniqueIdentifier(): Any = string

    override fun getContent(): String = toString()
}
