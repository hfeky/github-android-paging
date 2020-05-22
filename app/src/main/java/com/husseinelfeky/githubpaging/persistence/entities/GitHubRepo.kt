package com.husseinelfeky.githubpaging.persistence.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.husseinelfeky.githubpaging.common.paging.base.PagingItem

@Entity(
    tableName = "repositories",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
            deferred = true
        )
    ]
)
data class GitHubRepo(
    @PrimaryKey val id: Int,
    val userId: Int,
    val name: String
) : PagingItem {

    override fun getUniqueIdentifier(): Any = id

    override fun getContent(): String = toString()
}
