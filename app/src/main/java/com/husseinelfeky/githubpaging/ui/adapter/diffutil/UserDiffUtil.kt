package com.husseinelfeky.githubpaging.ui.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.husseinelfeky.githubpaging.persistence.entities.User

class UserDiffUtil : DiffUtil.ItemCallback<User>() {

    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }

    companion object {
        fun getInstance(): UserDiffUtil {
            return UserDiffUtil()
        }
    }
}
