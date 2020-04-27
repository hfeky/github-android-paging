package com.husseinelfeky.githubpaging.repository

import com.husseinelfeky.githubpaging.api.GitHubApi
import com.husseinelfeky.githubpaging.api.RetrofitClient
import com.husseinelfeky.githubpaging.persistence.AppRoomDatabase

object DataSource {
    val db = AppRoomDatabase.getDatabase().gitHubDao()
    val gitHubAPI: GitHubApi = RetrofitClient.getClient().create(GitHubApi::class.java)
}