package com.husseinelfeky.githubpaging.api

import com.husseinelfeky.githubpaging.persistence.entities.GitHubRepo
import com.husseinelfeky.githubpaging.persistence.entities.User
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

const val token = "be9c91c4359b0c0c0f35205aff776a524e251513"

interface GitHubApi {

    @Headers("Authorization: token$token")
    @GET("users")
    suspend fun getUsers(): List<User>

    @Headers("Authorization: token$token")
    @GET("users/{user_name}/repos")
    suspend fun getRepositories(@Path("user_name") userName: String): List<GitHubRepo>

    @Headers("Authorization: token$token")
    @GET("users")
    fun getUsersRx(@Query("page") page: Int, @Query("per_page") perPage: Int = 10): Single<List<User>>

    @Headers("Authorization: token$token")
    @GET("users/{user_name}/repos")
    fun getGitHubReposRx(
        @Path("user_name") userName: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 10
    ): Single<List<GitHubRepo>>

    @Headers("Authorization: token$token")
    @GET("users/{user_name}/repos")
    fun getRepositoriesRx(@Path("user_name") userName: String): Single<List<GitHubRepo>>
}
