package com.husseinelfeky.githubpaging.sectionedRecyclerView.bases

import androidx.recyclerview.widget.DiffUtil

class SectionItemDiffUtils<T: DiffUtilable>(private val oldList: List<T>, private val newList: List<T>): DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =  newList[oldItemPosition] == oldList[oldItemPosition]
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return newList[oldItemPosition].getUniqueIdentifier() == oldList[oldItemPosition].getUniqueIdentifier()
    }
}
