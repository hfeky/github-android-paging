package com.husseinelfeky.githubpaging.api

import com.husseinelfeky.githubpaging.database.entities.GitHubRepo
import com.husseinelfeky.githubpaging.database.entities.User
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApi {

    @GET("users")
    fun getUsers(
        @Query("since") sinceId: Int,
        @Query("per_page") pageSize: Int
    ): Single<List<User>>

    @GET("users/{user_name}/repos")
    fun getUserRepos(@Path("user_name") userName: String): Single<List<GitHubRepo>>
}
