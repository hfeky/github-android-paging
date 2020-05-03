package com.husseinelfeky.githubpaging.common.sectionedrecyclerview.bases

import androidx.recyclerview.widget.DiffUtil
import com.husseinelfeky.githubpaging.common.sectionedrecyclerview.SectionAdapter
import com.husseinelfeky.githubpaging.common.sectionedrecyclerview.SectionAdapterListUpdateCallback
import com.husseinelfeky.githubpaging.common.sectionedrecyclerview.SectionedRecyclerViewAdapter

open class BaseSectionAdapter : SectionedRecyclerViewAdapter() {

    /**
     * Add items to section.
     */
    fun <T : DiffUtilable> updateSection(section: BaseSection<T>) {
        val diffResult =
            DiffUtil.calculateDiff(SectionItemDiffUtils<T>(section.oldItems, section.items))
        val adapterListUpdateCallback =
            SectionAdapterListUpdateCallback(this.getAdapterForSection(section))
        diffResult.dispatchUpdatesTo(adapterListUpdateCallback)
    }

    /**
     * Get section adapter.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun <T : DiffUtilable> getAdapterForSection(section: BaseSection<T>): SectionAdapter {
        return SectionAdapter(this, section)
    }
}