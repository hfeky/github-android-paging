package com.husseinelfeky.githubpaging.persistence.dao

import androidx.paging.DataSource
import androidx.room.*
import com.husseinelfeky.githubpaging.persistence.entities.GitHubRepo
import com.husseinelfeky.githubpaging.persistence.entities.User
import com.husseinelfeky.githubpaging.persistence.entities.UserWithRepos
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface GitHubDao {

    @Transaction
    @Query("SELECT * FROM users ORDER BY userName")
    fun getUsersWithRepos(): DataSource.Factory<Int, UserWithRepos>

    @Query("SELECT * FROM users ORDER BY userName")
    fun getUsersWithReposRx(): Single<List<UserWithRepos>>

    @Query("DELETE FROM users")
    fun deleteAllRx(): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsersRx(groupAList: List<User>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReposRx(gitHubRepo: GitHubRepo): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepo(gitHubRepo: GitHubRepo)
}
