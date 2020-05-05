package com.husseinelfeky.githubpaging.api

import com.husseinelfeky.githubpaging.persistence.entities.GitHubRepo
import com.husseinelfeky.githubpaging.persistence.entities.User
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApi {

    /**
     * GitHub Users API endpoint does not support pagination by page; it paginates
     * with respect to the user's id.
     *
     * @param sinceId Set it to "(pageToFetch - 1) * pageSize" to solve this issue.
     */
    @GET("users")
    fun getUsers(
        @Query("since") sinceId: Int,
        @Query("per_page") pageSize: Int
    ): Single<List<User>>

    @GET("users/{user_name}/repos")
    fun getUserRepos(@Path("user_name") userName: String): Single<List<GitHubRepo>>
}
