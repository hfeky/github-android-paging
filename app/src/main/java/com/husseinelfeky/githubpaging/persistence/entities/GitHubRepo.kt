package com.husseinelfeky.githubpaging.persistence.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.husseinelfeky.githubpaging.models.PagingItem

@Entity(
    tableName = "repositories",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"]
        )
    ]
)
data class GitHubRepo(
    @PrimaryKey override val id: Long,
    val userId: Long,
    val name: String
) : PagingItem()
