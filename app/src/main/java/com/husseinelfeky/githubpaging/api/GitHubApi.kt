package com.husseinelfeky.githubpaging.api

import com.husseinelfeky.githubpaging.models.responses.GitHubRepoResponse
import com.husseinelfeky.githubpaging.models.responses.UserResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubApi {

    @GET("users")
    suspend fun getUsers(): List<UserResponse>

    @GET("users/{user_name}/repos")
    suspend fun getRepositories(@Path("user_name") userName: String): List<GitHubRepoResponse>
}
