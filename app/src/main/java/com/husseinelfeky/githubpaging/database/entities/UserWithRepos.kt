package com.husseinelfeky.githubpaging.database.entities

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithRepos(
    @Embedded
    val user: User,
    @Relation(parentColumn = "id", entityColumn = "userId")
    val repos: List<GitHubRepo>
)
