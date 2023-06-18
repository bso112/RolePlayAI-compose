package com.bso112.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class RolePlayService(
    private val client: HttpClient = KtorClient.httpClient
) {

    suspend fun sendChat(
        message: String
    ): String {
        val url = "https://platform.openai.com/docs/guides/gpt/chat-completions-api"
        return client.get(url).body()
    }
}