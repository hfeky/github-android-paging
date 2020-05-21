package com.husseinelfeky.githubpaging.persistence.dao

import androidx.room.*
import com.husseinelfeky.githubpaging.persistence.entities.GitHubRepo
import com.husseinelfeky.githubpaging.persistence.entities.User
import com.husseinelfeky.githubpaging.persistence.entities.UserWithRepos
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface GitHubDao {

    @Transaction
    @Query("SELECT * FROM users ORDER BY id LIMIT :itemsLimit")
    fun getUsersWithRepos(itemsLimit: Long): Flowable<List<UserWithRepos>>

    @Query("SELECT * FROM users ORDER BY id LIMIT :pageSize OFFSET :offset")
    fun getUsers(pageSize: Int, offset: Long): Single<List<User>>

    @Query("SELECT * FROM repositories WHERE userId = :userId ORDER BY name")
    fun getUserRepos(userId: Long): Single<List<GitHubRepo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsers(users: List<User>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRepos(gitHubRepos: List<GitHubRepo>): Completable

    @Query("DELETE FROM users")
    fun deleteAllUsers(): Completable

    @Query("DELETE FROM repositories")
    fun deleteAllRepos(): Completable
}
