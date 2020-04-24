package com.husseinelfeky.githubpaging.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.husseinelfeky.githubpaging.R
import com.husseinelfeky.githubpaging.persistence.entities.GitHubRepo
import com.husseinelfeky.githubpaging.persistence.entities.UserWithRepos
import com.husseinelfeky.githubpaging.sectionedRecyclerView.bases.BaseSection
import com.husseinelfeky.githubpaging.ui.viewholder.RepoViewHolder
import com.husseinelfeky.githubpaging.ui.viewholder.UserViewHolder
import kotlinx.android.synthetic.main.item_repo.view.*
import kotlinx.android.synthetic.main.item_user.view.*

class UserWithReposSection(private val userWithRepos: UserWithRepos): BaseSection<GitHubRepo>(userWithRepos.repos.toMutableList(), SectionUtils.getSectionParams()) {

    val id: String = userWithRepos.user.id.toString()

    override fun getHeaderViewHolder(view: View): RecyclerView.ViewHolder {
        return UserViewHolder(view)
    }

    override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder) {
        val headerHolder = holder as UserViewHolder
        val itemView = headerHolder.itemView
        // Populate GitHub user data
        userWithRepos.user.apply {
            itemView.user_name.text = this.userName
            itemView.user_id.text = this.id.toString()
            Glide.with(itemView.context)
                .load(this.avatarUrl)
                .transform(CircleCrop())
                .transition(DrawableTransitionOptions.withCrossFade(250))
                .placeholder(R.drawable.placeholder_person)
                .into(itemView.user_photo)
        }

        itemView.rv_repos.adapter = ReposAdapter().apply {
            submitList(userWithRepos.repos)
        }
    }

    override fun getItemViewHolder(view: View): RecyclerView.ViewHolder {
        return RepoViewHolder(view)
    }

    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemHolder = holder as RepoViewHolder
        val itemView = itemHolder.itemView
        userWithRepos.repos[position].apply {
            itemView.repo_name.text = this.name
        }
    }

    //    override fun getEmptyViewHolder(view: View?): RecyclerView.ViewHolder {
//        return super.getEmptyViewHolder(view)
//    }
}