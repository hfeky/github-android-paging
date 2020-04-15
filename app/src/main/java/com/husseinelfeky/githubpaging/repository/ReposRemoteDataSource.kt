package com.husseinelfeky.githubpaging.repository

import com.husseinelfeky.githubpaging.api.GitHubApi
import com.husseinelfeky.githubpaging.api.RetrofitClient
import com.husseinelfeky.githubpaging.models.responses.GitHubRepoResponse
import com.husseinelfeky.githubpaging.models.responses.UserResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ReposRemoteDataSource {

    private val gitHubApi = RetrofitClient.getClient().create(GitHubApi::class.java)

    fun getUsers(): Flow<List<UserResponse>> = flow {
        emit(
            gitHubApi.getUsers()
        )
    }

    fun getRepositories(userName: String): Flow<List<GitHubRepoResponse>> = flow {
        emit(
            gitHubApi.getRepositories(userName)
        )
    }
}
