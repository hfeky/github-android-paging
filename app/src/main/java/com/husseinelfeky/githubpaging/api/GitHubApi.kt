package com.husseinelfeky.githubpaging.api

import com.husseinelfeky.githubpaging.persistence.entities.GitHubRepo
import com.husseinelfeky.githubpaging.persistence.entities.User
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApi {

    /**
     * Users API endpoint does not support pagination by page. Setting
     * @param pageSize to "actualPageSize * pageToFetch" will solve this issue.
     */
    @GET("users")
    fun getUsersRx(
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): Single<List<User>>

    @GET("users/{user_name}/repos")
    fun getAllRepositoriesRx(@Path("user_name") userName: String): Single<List<GitHubRepo>>
}
