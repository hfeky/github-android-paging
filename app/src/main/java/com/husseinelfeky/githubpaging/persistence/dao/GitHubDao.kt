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
    @Query("SELECT * FROM users ORDER BY userName")
    fun getUsersWithReposRx(): Flowable<List<UserWithRepos>>

    @Query("SELECT * FROM users ORDER BY userName")
    fun getUsersRx(): Single<List<User>>

    @Query("SELECT * FROM repositories ORDER BY name")
    fun getReposRx(): Single<List<GitHubRepo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsersRx(users: List<User>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReposRx(gitHubRepos: List<GitHubRepo>): Completable

    @Query("DELETE FROM users")
    fun deleteAllUsersRx(): Completable

    @Query("DELETE FROM repositories")
    fun deleteAllReposRx(): Completable
}
