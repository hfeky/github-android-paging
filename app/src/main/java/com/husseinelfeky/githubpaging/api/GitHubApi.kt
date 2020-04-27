package com.husseinelfeky.githubpaging.api

import com.husseinelfeky.githubpaging.models.responses.GitHubRepoResponse
import com.husseinelfeky.githubpaging.models.responses.UserResponse
import com.husseinelfeky.githubpaging.persistence.entities.GitHubRepo
import com.husseinelfeky.githubpaging.persistence.entities.User
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApi {

    @Headers("Authorization: token ac39d4e7646a2dab6d33950abe058595a0b2312d")
    @GET("users")
    suspend fun getUsers(): List<UserResponse>

    @Headers("Authorization: token ac39d4e7646a2dab6d33950abe058595a0b2312d")
    @GET("users/{user_name}/repos")
    suspend fun getRepositories(@Path("user_name") userName: String): List<GitHubRepoResponse>

    @Headers("Authorization: token ac39d4e7646a2dab6d33950abe058595a0b2312d")
    @GET("users")
    fun getUsersRx(@Query("page") page: Int, @Query("per_page") perPage: Int = 20): Single<List<User>>

    @Headers("Authorization: token ac39d4e7646a2dab6d33950abe058595a0b2312d")
    @GET("users/{user_name}/repos")
    fun getGitHubReposRx(@Path("user_name") userName: String,
                         @Query("page") page: Int,
                         @Query("per_page") perPage: Int = 20): Single<List<GitHubRepo>>

    @Headers("Authorization: token ac39d4e7646a2dab6d33950abe058595a0b2312d")
    @GET("users/{user_name}/repos")
    fun getRepositoriesRx(@Path("user_name") userName: String): Single<List<GitHubRepoResponse>>
}
