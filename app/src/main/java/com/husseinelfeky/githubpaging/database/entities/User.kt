package com.husseinelfeky.githubpaging.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.husseinelfeky.githubpaging.paging.base.PagingItem
import com.squareup.moshi.Json

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: Int,
    @Json(name = "login")
    val userName: String,
    @Json(name = "avatar_url")
    val avatarUrl: String
) : PagingItem {

    override fun getUniqueIdentifier(): Any = id

    override fun getContent(): String = toString()
}
