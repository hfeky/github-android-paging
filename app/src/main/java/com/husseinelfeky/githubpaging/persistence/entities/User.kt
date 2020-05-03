package com.husseinelfeky.githubpaging.persistence.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.husseinelfeky.githubpaging.models.PagingItem
import com.squareup.moshi.Json

@Entity(tableName = "users")
data class User(
    @PrimaryKey override val id: Long,
    @Json(name = "login")
    val userName: String,
    @Json(name = "avatar_url")
    val avatarUrl: String
) : PagingItem()
