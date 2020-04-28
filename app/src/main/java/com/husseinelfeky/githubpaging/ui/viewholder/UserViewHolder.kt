package com.husseinelfeky.githubpaging.ui.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.husseinelfeky.githubpaging.R
import com.husseinelfeky.githubpaging.persistence.entities.UserWithRepos
import com.husseinelfeky.githubpaging.ui.ReposAdapter
import kotlinx.android.synthetic.main.item_user.view.*

class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(userWithRepos: UserWithRepos, recycledViewPool: RecyclerView.RecycledViewPool) {
        val user = userWithRepos.user

        itemView.user_name.text = user.userName
        itemView.user_id.text = user.id.toString()

        Glide.with(itemView.context)
            .load(user.avatarUrl)
            .transform(CircleCrop())
            .transition(DrawableTransitionOptions.withCrossFade(250))
            .placeholder(R.drawable.placeholder_person)
            .into(itemView.user_photo)

        itemView.rv_repos.setRecycledViewPool(recycledViewPool)
        itemView.rv_repos.adapter = ReposAdapter().apply {
            submitList(userWithRepos.repos)
        }
    }

    companion object {
        fun create(parent: ViewGroup): UserViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_user, parent, false)
            return UserViewHolder(view)
        }
    }
}
