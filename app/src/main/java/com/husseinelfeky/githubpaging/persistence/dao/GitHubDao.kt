package com.husseinelfeky.githubpaging.persistence.dao

import androidx.paging.DataSource
import androidx.room.*
import com.husseinelfeky.githubpaging.persistence.entities.GitHubRepo
import com.husseinelfeky.githubpaging.persistence.entities.User
import com.husseinelfeky.githubpaging.persistence.entities.UserWithRepos

@Dao
interface GitHubDao {

    @Transaction
    @Query("SELECT * FROM users ORDER BY userName")
    fun getUsersWithRepos(): DataSource.Factory<Int, UserWithRepos>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepo(gitHubRepo: GitHubRepo)
}
