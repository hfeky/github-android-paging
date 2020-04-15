package com.husseinelfeky.githubpaging.persistence.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.husseinelfeky.githubpaging.models.PagingItem

@Entity(tableName = "users")
data class User(
    @PrimaryKey override val id: Long,
    val userName: String,
    val avatarUrl: String
) : PagingItem()
