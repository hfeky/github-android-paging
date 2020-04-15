package com.husseinelfeky.githubpaging.ui.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.husseinelfeky.githubpaging.R
import com.husseinelfeky.githubpaging.persistence.entities.GitHubRepo
import kotlinx.android.synthetic.main.item_repo.view.*

class RepoViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(gitHubRepo: GitHubRepo) {
        itemView.repo_name.text = gitHubRepo.name
    }

    companion object {
        fun create(parent: ViewGroup): RepoViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_repo, parent, false)
            return RepoViewHolder(view)
        }
    }
}
