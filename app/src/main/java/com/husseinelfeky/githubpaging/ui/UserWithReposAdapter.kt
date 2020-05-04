package com.husseinelfeky.githubpaging.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.husseinelfeky.githubpaging.R
import com.husseinelfeky.githubpaging.common.paging.base.PagingAdapter
import com.husseinelfeky.githubpaging.common.paging.base.PagingItem
import com.husseinelfeky.githubpaging.persistence.entities.GitHubRepo
import com.husseinelfeky.githubpaging.persistence.entities.User
import com.husseinelfeky.githubpaging.persistence.entities.UserWithRepos
import com.husseinelfeky.githubpaging.ui.viewholder.RepoViewHolder
import com.husseinelfeky.githubpaging.ui.viewholder.UserViewHolder
import timber.log.Timber

class UserWithReposAdapter : PagingAdapter<UserWithRepos>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_user -> UserViewHolder.create(parent)
            R.layout.item_repo -> RepoViewHolder.create(parent)
            else -> throw IllegalArgumentException("Unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_user -> {
                (holder as UserViewHolder).bind(getItem(position) as User)
            }
            R.layout.item_repo -> {
                (holder as RepoViewHolder).bind(getItem(position) as GitHubRepo)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position) is User) {
            R.layout.item_user
        } else {
            R.layout.item_repo
        }
    }

    override fun updateList(list: List<UserWithRepos>) {
        val pagingItems = mutableListOf<PagingItem>()
        list.forEach { userWithRepos ->
            with(userWithRepos) {
                if (repos.isNotEmpty()) {
                    pagingItems.add(user)
                    pagingItems.addAll(repos)
                }
            }
        }
        Timber.i("pagingItems size is ${pagingItems.size}")
        submitList(pagingItems)
    }
}
