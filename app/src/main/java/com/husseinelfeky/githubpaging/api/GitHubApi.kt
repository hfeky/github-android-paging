package com.husseinelfeky.githubpaging.api

import com.husseinelfeky.githubpaging.models.responses.GitHubRepoResponse
import com.husseinelfeky.githubpaging.models.responses.UserResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface GitHubApi {

    @Headers("Authorization: token ac39d4e7646a2dab6d33950abe058595a0b2312d")
    @GET("users")
    suspend fun getUsers(): List<UserResponse>

    @Headers("Authorization: token ac39d4e7646a2dab6d33950abe058595a0b2312d")
    @GET("users/{user_name}/repos")
    suspend fun getRepositories(@Path("user_name") userName: String): List<GitHubRepoResponse>
}
