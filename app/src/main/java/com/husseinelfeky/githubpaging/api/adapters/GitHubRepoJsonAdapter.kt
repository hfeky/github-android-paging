package com.husseinelfeky.githubpaging.api.adapters

import com.husseinelfeky.githubpaging.persistence.entities.GitHubRepo
import com.squareup.moshi.*

class GitHubRepoJsonAdapter : JsonAdapter<GitHubRepo>() {

    @FromJson
    override fun fromJson(reader: JsonReader): GitHubRepo? {
        val jsonMap = reader.readJsonValue() as Map<*, *>
        val user = jsonMap["owner"] as Map<*, *>
        return GitHubRepo(
            jsonMap["id"] as Long,
            user["id"] as Long,
            jsonMap["name"] as String
        )
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: GitHubRepo?) {
        throw NotImplementedError("This adapter cannot convert GitHubRepo to JSON")
    }
}
