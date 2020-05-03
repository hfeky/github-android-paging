package com.husseinelfeky.githubpaging.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.husseinelfeky.githubpaging.R
import com.husseinelfeky.githubpaging.common.sectionedrecyclerview.SectionParameters
import com.husseinelfeky.githubpaging.common.sectionedrecyclerview.bases.BaseSection
import com.husseinelfeky.githubpaging.common.sectionedrecyclerview.utils.SectionUtils
import com.husseinelfeky.githubpaging.persistence.entities.GitHubRepo
import com.husseinelfeky.githubpaging.persistence.entities.UserWithRepos
import com.husseinelfeky.githubpaging.ui.viewholder.RepoViewHolder
import com.husseinelfeky.githubpaging.ui.viewholder.UserViewHolder

class UserWithReposSection(private val userWithRepos: UserWithRepos) : BaseSection<GitHubRepo>(
    userWithRepos.repos.toMutableList(),
    getSectionParams()
) {

    val id: String = userWithRepos.user.id.toString()

    override fun getHeaderViewHolder(view: View): RecyclerView.ViewHolder {
        return UserViewHolder(view)
    }

    override fun getItemViewHolder(view: View): RecyclerView.ViewHolder {
        return RepoViewHolder(view)
    }

    override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder) {
        (holder as UserViewHolder).bind(userWithRepos.user)
    }

    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as RepoViewHolder).bind(userWithRepos.repos[position])
    }

    companion object {
        fun getSectionParams(): SectionParameters {
            return SectionUtils.getSectionWithParamsBuilderWith(
                headerLayout = R.layout.item_user,
                childLayout = R.layout.item_repo
            )
//                .loadingResourceId(R.layout.item_loading)
//                .emptyResourceId(R.layout.layout_empty_repos)
//                .failedResourceId(R.layout.layout_unknown_error)
                .build()
        }
    }
}
