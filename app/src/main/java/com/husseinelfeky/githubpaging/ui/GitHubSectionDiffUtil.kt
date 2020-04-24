package com.husseinelfeky.githubpaging.ui

import androidx.recyclerview.widget.DiffUtil
import com.husseinelfeky.githubpaging.persistence.entities.UserWithRepos

class GitHubSectionDiffUtil: DiffUtil.ItemCallback<UserWithRepos>() {

    override fun areItemsTheSame(oldItem: UserWithRepos, newItem: UserWithRepos): Boolean {
        return oldItem.user.id == newItem.user.id
    }

    override fun areContentsTheSame(oldItem: UserWithRepos, newItem: UserWithRepos): Boolean {
        return oldItem == newItem
    }

    companion object {
        fun instance(): GitHubUserDiffUtil {
            return GitHubUserDiffUtil()
        }
    }
}