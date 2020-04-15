package com.husseinelfeky.githubpaging.models.responses

import com.squareup.moshi.Json

data class UserResponse(
    val id: Long,
    @Json(name = "login") val userName: String,
    @Json(name = "avatar_url") val avatarUrl: String
)
