package com.husseinelfeky.githubpaging.api

import com.husseinelfeky.githubpaging.persistence.entities.GitHubRepo
import com.husseinelfeky.githubpaging.persistence.entities.User
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApi {

    @GET("users")
    suspend fun getUsers(): List<User>

    @GET("users/{user_name}/repos")
    suspend fun getRepositories(@Path("user_name") userName: String): List<GitHubRepo>

    @GET("users")
    fun getUsersRx(@Query("page") page: Int, @Query("per_page") perPage: Int = 2): Single<List<User>>

    @GET("users/{user_name}/repos")
    fun getRepositoriesRx(
        @Path("user_name") userName: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 10
    ): Single<List<GitHubRepo>>

    @GET("users/{user_name}/repos")
    fun getAllRepositoriesRx(@Path("user_name") userName: String): Single<List<GitHubRepo>>
}
