package com.husseinelfeky.githubpaging.sectionedRecyclerView

import android.content.ClipData.Item
import androidx.paging.AsyncPagedListDiffer
import androidx.paging.PagedList
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView

abstract class PagedListSection<S: Section>(private val sectionedAdapter: SectionedRecyclerViewAdapter, section: Section?, diffUtil: DiffUtil.ItemCallback<S>) : SectionAdapter(sectionedAdapter, section) {

    private val listUpdateCallback: ListUpdateCallback

    init {
        @Suppress("LeakingThis")
        listUpdateCallback = SectionAdapterListUpdateCallback(this)
    }

    private val differ: AsyncPagedListDiffer<S> = AsyncPagedListDiffer(sectionedAdapter, diffUtil)

//    private val differ: AsyncPagedListDiffer<S> = object : AsyncPagedListDiffer<S>(sectionedAdapter,
//            AsyncDifferConfig.Builder<S>(object : DiffUtil.ItemCallback<S>() {
//                override fun areItemsTheSame(oldItem: S, newItem: S): Boolean {
//                    return newItem.isSameAs(oldItem)
//                }
//
//                override fun areContentsTheSame(oldItem: S, newItem: S): Boolean {
//                    return newItem.equals(oldItem)
//                }
//            }).build()
//    )

    open fun submitList(newPagedList: PagedList<S>?) {
        differ.submitList(newPagedList)
        newPagedList.dataSource.
        listUpdateCallback.
    }

//    open fun getItemCount(): Int {
//        return differ.itemCount
//    }
//
//    open fun getItem(position: Int): Item? {
//        val item: Item? = differ.getItem(position)
//        if (item != null) {
//            // TODO find more efficiency registration timing, and removing observer
//            item.registerGroupDataObserver(this)
//            return item
//        }
//        return placeHolder
//    }
//
//    open fun getPosition(item: Item): Int {
//        val currentList = differ.currentList ?: return -1
//        return currentList.indexOf(item)
//    }
//
//    open fun onChanged(section: Section?) {
//        parentObserver.onChanged(this)
//    }
//
//    open fun onItemInserted(section: Section, position: Int) {
//        val index = getItemPosition(section)
//        if (index >= 0 && parentObserver != null) {
//            parentObserver.onItemInserted(this, index)
//        }
//    }
//
//    open fun onItemChanged(section: Section, position: Int) {
//        val index = getItemPosition(section)
//        if (index >= 0 && parentObserver != null) {
//            parentObserver.onItemChanged(this, index)
//        }
//    }
//
//    open fun onItemChanged(section: Section, position: Int, payload: Any?) {
//        val index = getItemPosition(section)
//        if (index >= 0 && parentObserver != null) {
//            parentObserver.onItemChanged(this, index, payload)
//        }
//    }
//
//    open fun onItemRemoved(section: Section, position: Int) {
//        val index = getItemPosition(section)
//        if (index >= 0 && parentObserver != null) {
//            parentObserver.onItemRemoved(this, index)
//        }
//    }
//
//    open fun onItemRangeChanged(section: Section, positionStart: Int, itemCount: Int) {
//        val index = getItemPosition(section)
//        if (index >= 0 && parentObserver != null) {
//            parentObserver.onItemRangeChanged(this, index, itemCount)
//        }
//    }
//
//    open fun onItemRangeChanged(section: Section, positionStart: Int, itemCount: Int, payload: Any?) {
//        val index = getItemPosition(section)
//        if (index >= 0 && parentObserver != null) {
//            parentObserver.onItemRangeChanged(this, index, itemCount, payload)
//        }
//    }
//
//    open fun onItemRangeInserted(section: Section, positionStart: Int, itemCount: Int) {
//        val index = getItemPosition(section)
//        if (index >= 0 && parentObserver != null) {
//            parentObserver.onItemRangeInserted(this, index, itemCount)
//        }
//    }
//
//    open fun onItemRangeRemoved(section: Section, positionStart: Int, itemCount: Int) {
//        val index = getItemPosition(section)
//        if (index >= 0 && parentObserver != null) {
//            parentObserver.onItemRangeRemoved(this, index, itemCount)
//        }
//    }
//
//    open fun onItemMoved(section: Section, fromPosition: Int, toPosition: Int) {
//        val index = getItemPosition(section)
//        if (index >= 0 && parentObserver != null) {
//            parentObserver.onItemRangeChanged(this, index, toPosition)
//        }
//    }

    open fun getItemPosition(section: Section): Int {
        val currentList = differ.currentList ?: return -1
        return currentList.indexOf(section)
    }
}