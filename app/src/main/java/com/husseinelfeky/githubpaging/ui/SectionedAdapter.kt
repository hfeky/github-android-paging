package com.husseinelfeky.githubpaging.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.husseinelfeky.githubpaging.R
import com.husseinelfeky.githubpaging.common.NetworkState
import com.husseinelfeky.githubpaging.persistence.entities.UserWithRepos
import com.husseinelfeky.githubpaging.sectionedRecyclerView.SectionedRecyclerViewAdapter
import com.husseinelfeky.githubpaging.ui.viewholder.LoadingViewHolder
import com.husseinelfeky.githubpaging.ui.viewholder.UserViewHolder

class SectionedAdapter: SectionedRecyclerViewAdapter<UserWithRepos, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_user -> UserViewHolder.create(parent)
            R.layout.item_loading -> LoadingViewHolder.create(parent)
            else -> throw IllegalArgumentException("Unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_user -> {
                (holder as UserViewHolder).bind(getItem(position)!!)
            }
            R.layout.item_loading -> {
                (holder as LoadingViewHolder).bindTo(networkState)
            }
        }
    }

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.item_loading
        } else {
            R.layout.item_user
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    fun setNetworkState(newNetworkState: NetworkState?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<UserWithRepos> =
                object : DiffUtil.ItemCallback<UserWithRepos>() {
                    override fun areItemsTheSame(
                            oldItem: UserWithRepos,
                            newItem: UserWithRepos
                    ): Boolean {
                        return oldItem.user.id == newItem.user.id &&
                                oldItem.repos == newItem.repos
                    }

                    override fun areContentsTheSame(
                            oldItem: UserWithRepos,
                            newItem: UserWithRepos
                    ): Boolean {
                        return oldItem == newItem
                    }
                }
    }
}
