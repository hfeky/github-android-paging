package com.husseinelfeky.githubpaging.ui.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.husseinelfeky.githubpaging.persistence.entities.UserWithRepos

class UserWithReposDiffUtil : DiffUtil.ItemCallback<UserWithRepos>() {

    override fun areItemsTheSame(oldItem: UserWithRepos, newItem: UserWithRepos): Boolean {
        return oldItem.user.id == newItem.user.id
                && oldItem.repos == newItem.repos
    }

    override fun areContentsTheSame(oldItem: UserWithRepos, newItem: UserWithRepos): Boolean {
        return oldItem == newItem
    }

    companion object {
        fun getInstance(): UserWithReposDiffUtil {
            return UserWithReposDiffUtil()
        }
    }
}
