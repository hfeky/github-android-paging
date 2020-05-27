package com.husseinelfeky.githubpaging.paging.utils

import androidx.recyclerview.widget.DiffUtil
import com.husseinelfeky.githubpaging.paging.base.PagingItem

class PagingItemDiffUtil : DiffUtil.ItemCallback<PagingItem>() {

    override fun areItemsTheSame(oldItem: PagingItem, newItem: PagingItem): Boolean {
        return oldItem.getUniqueIdentifier() == newItem.getUniqueIdentifier()
    }

    override fun areContentsTheSame(oldItem: PagingItem, newItem: PagingItem): Boolean {
        return oldItem.getContent() == newItem.getContent()
    }

    companion object {
        fun getInstance(): PagingItemDiffUtil {
            return PagingItemDiffUtil()
        }
    }
}
