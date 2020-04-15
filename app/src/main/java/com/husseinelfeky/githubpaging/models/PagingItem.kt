package com.husseinelfeky.githubpaging.models

abstract class PagingItem {

    abstract val id: Long

    override fun equals(other: Any?): Boolean {
        if (other is PagingItem) {
            return id == other.id
        }
        return false
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
