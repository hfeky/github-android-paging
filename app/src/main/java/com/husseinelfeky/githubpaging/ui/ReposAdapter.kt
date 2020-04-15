package com.husseinelfeky.githubpaging.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.husseinelfeky.githubpaging.persistence.entities.GitHubRepo
import com.husseinelfeky.githubpaging.ui.viewholder.RepoViewHolder

class ReposAdapter : ListAdapter<GitHubRepo, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RepoViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as RepoViewHolder).bind(getItem(position) as GitHubRepo)
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<GitHubRepo> =
            object : DiffUtil.ItemCallback<GitHubRepo>() {
                override fun areItemsTheSame(
                    oldItem: GitHubRepo,
                    newItem: GitHubRepo
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: GitHubRepo,
                    newItem: GitHubRepo
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
