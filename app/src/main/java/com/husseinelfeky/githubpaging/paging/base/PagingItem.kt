package com.husseinelfeky.githubpaging.paging.base

interface PagingItem {

    fun getUniqueIdentifier(): Any

    fun getContent(): String
}
