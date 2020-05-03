package com.husseinelfeky.githubpaging.ui.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.husseinelfeky.githubpaging.R
import com.husseinelfeky.githubpaging.persistence.entities.User
import kotlinx.android.synthetic.main.item_user.view.*

class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(user: User) {
        itemView.user_name.text = user.userName
        itemView.user_id.text = user.id.toString()

        Glide.with(itemView.context)
            .load(user.avatarUrl)
            .transform(CircleCrop())
            .transition(DrawableTransitionOptions.withCrossFade(250))
            .placeholder(R.drawable.placeholder_person)
            .into(itemView.user_photo)
    }

    companion object {
        fun create(parent: ViewGroup): UserViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_user, parent, false)
            return UserViewHolder(view)
        }
    }
}
