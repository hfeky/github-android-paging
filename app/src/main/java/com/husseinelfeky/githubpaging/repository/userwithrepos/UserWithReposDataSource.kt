package com.husseinelfeky.githubpaging.repository.userwithrepos

import com.husseinelfeky.githubpaging.api.GitHubApi
import com.husseinelfeky.githubpaging.api.RetrofitClient
import com.husseinelfeky.githubpaging.persistence.AppRoomDatabase

object UserWithReposDataSource {

    val gitHubDao = AppRoomDatabase.getDatabase().gitHubDao()

    val gitHubApi: GitHubApi = RetrofitClient.getClient().create(GitHubApi::class.java)
}
