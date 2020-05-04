package com.husseinelfeky.githubpaging.common.paging.base

import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.husseinelfeky.githubpaging.common.paging.adapter.PagingItemDiffUtil

abstract class PagingAdapter<Entity> : ListAdapter<PagingItem, RecyclerView.ViewHolder>(
    PagingItemDiffUtil.getInstance()
) {

    abstract fun updateList(list: List<Entity>)
}
