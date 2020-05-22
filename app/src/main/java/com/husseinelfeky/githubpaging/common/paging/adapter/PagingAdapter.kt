package com.husseinelfeky.githubpaging.common.paging.adapter

import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.husseinelfeky.githubpaging.common.paging.base.PagingItem
import com.husseinelfeky.githubpaging.common.paging.utils.PagingItemDiffUtil
import kotlinx.coroutines.*

abstract class PagingAdapter<Entity> : ListAdapter<PagingItem, RecyclerView.ViewHolder>(
    PagingItemDiffUtil.getInstance()
) {

    private val uiDispatcher = Dispatchers.Main
    private val backgroundScope = CoroutineScope(Dispatchers.Default)

    private var conversionJob: Job? = null

    private var onListUpdatedCallback: (() -> Unit)? = null

    fun setOnListUpdatedCallback(onListUpdatedCallback: () -> Unit) {
        this.onListUpdatedCallback = onListUpdatedCallback
    }

    fun updateList(list: List<Entity>) {
        conversionJob?.let { job ->
            if (job.isActive) {
                job.cancel("A new list is populated.")
            }
        }

        conversionJob = backgroundScope.launch {
            convertList(list).also { pagingItems ->
                withContext(uiDispatcher) {
                    submitList(pagingItems, onListUpdatedCallback)
                }
            }
        }
    }

    abstract fun convertList(list: List<Entity>): List<PagingItem>
}
