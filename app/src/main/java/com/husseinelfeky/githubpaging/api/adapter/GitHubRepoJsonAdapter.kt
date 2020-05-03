package com.husseinelfeky.githubpaging.api.adapter

import com.husseinelfeky.githubpaging.persistence.entities.GitHubRepo
import com.squareup.moshi.*

class GitHubRepoJsonAdapter : JsonAdapter<GitHubRepo>() {

    @FromJson
    override fun fromJson(reader: JsonReader): GitHubRepo? {
        val jsonMap = reader.readJsonValue() as Map<*, *>
        val user = jsonMap["owner"] as Map<*, *>
        return GitHubRepo(
            (jsonMap["id"] as Double).toLong(),
            (user["id"] as Double).toLong(),
            jsonMap["name"] as String
        )
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: GitHubRepo?) {
        throw NotImplementedError("This adapter cannot convert GitHubRepo to JSON")
    }
}
