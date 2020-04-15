package com.husseinelfeky.githubpaging.repository

import androidx.paging.DataSource
import com.husseinelfeky.githubpaging.models.responses.GitHubRepoResponse
import com.husseinelfeky.githubpaging.models.responses.UserResponse
import com.husseinelfeky.githubpaging.persistence.dao.GitHubDao
import com.husseinelfeky.githubpaging.persistence.entities.GitHubRepo
import com.husseinelfeky.githubpaging.persistence.entities.User
import com.husseinelfeky.githubpaging.persistence.entities.UserWithRepos
import kotlinx.coroutines.flow.Flow

class ReposRepository(gitHubDao: GitHubDao) {

    private val localDataSource = ReposLocalDataSource(gitHubDao)
    private val remoteDataSource = ReposRemoteDataSource()

    fun getUsersWithRepos(): DataSource.Factory<Int, UserWithRepos> =
        localDataSource.getUsersWithRepos()

    fun getUsersRemotely(): Flow<List<UserResponse>> =
        remoteDataSource.getUsers()

    fun getRepositoriesRemotely(userName: String): Flow<List<GitHubRepoResponse>> =
        remoteDataSource.getRepositories(userName)

    suspend fun saveUser(user: User) =
        localDataSource.saveUser(user)

    suspend fun saveRepo(gitHubRepo: GitHubRepo) =
        localDataSource.saveRepo(gitHubRepo)
}
