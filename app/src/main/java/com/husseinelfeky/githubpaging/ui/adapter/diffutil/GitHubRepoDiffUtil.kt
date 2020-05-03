package com.husseinelfeky.githubpaging.ui.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.husseinelfeky.githubpaging.persistence.entities.GitHubRepo

class GitHubRepoDiffUtil : DiffUtil.ItemCallback<GitHubRepo>() {

    override fun areItemsTheSame(oldItem: GitHubRepo, newItem: GitHubRepo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: GitHubRepo, newItem: GitHubRepo): Boolean {
        return oldItem == newItem
    }

    companion object {
        fun getInstance(): GitHubRepoDiffUtil {
            return GitHubRepoDiffUtil()
        }
    }
}
