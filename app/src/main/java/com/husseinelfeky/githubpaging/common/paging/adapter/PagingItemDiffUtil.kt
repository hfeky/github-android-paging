package com.husseinelfeky.githubpaging.common.paging.adapter

import androidx.recyclerview.widget.DiffUtil
import com.husseinelfeky.githubpaging.models.PagingItem

class PagingItemDiffUtil : DiffUtil.ItemCallback<PagingItem>() {

    override fun areItemsTheSame(oldItem: PagingItem, newItem: PagingItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PagingItem, newItem: PagingItem): Boolean {
        return oldItem == newItem
    }

    companion object {
        fun getInstance(): PagingItemDiffUtil {
            return PagingItemDiffUtil()
        }
    }
}
