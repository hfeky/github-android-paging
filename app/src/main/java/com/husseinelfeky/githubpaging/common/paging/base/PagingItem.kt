package com.husseinelfeky.githubpaging.common.paging.base

interface PagingItem {

    fun getUniqueIdentifier(): Any

    fun getContent(): String
}
