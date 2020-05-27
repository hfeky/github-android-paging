package com.husseinelfeky.githubpaging.database.dao

import androidx.room.*
import com.husseinelfeky.githubpaging.database.entities.GitHubRepo
import com.husseinelfeky.githubpaging.database.entities.User
import com.husseinelfeky.githubpaging.database.entities.UserWithRepos
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface GitHubDao {

    @Transaction
    @Query("SELECT * FROM users ORDER BY id LIMIT :itemsLimit")
    fun getUsersWithRepos(itemsLimit: Int): Flowable<List<UserWithRepos>>

    @Query("SELECT * FROM users ORDER BY id LIMIT :pageSize OFFSET :offset")
    fun getUsers(pageSize: Int, offset: Int): Single<List<User>>

    @Query("SELECT * FROM repositories WHERE userId = :userId ORDER BY name")
    fun getUserRepos(userId: Int): Single<List<GitHubRepo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsers(users: List<User>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRepos(gitHubRepos: List<GitHubRepo>): Completable

    @Query("DELETE FROM users")
    fun deleteAllUsers(): Completable

    @Query("DELETE FROM repositories")
    fun deleteAllRepos(): Completable
}
