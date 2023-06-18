package com.bso112.data

import com.bso112.data.request.ChatRequest
import com.bso112.data.request.Model
import com.bso112.data.response.ChatResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType

class RolePlayService(
    private val client: HttpClient = KtorClient.httpClient
) {

    suspend fun sendChat(
        message: String
    ): Result<ChatResponse> = result {
        val url = "https://api.openai.com/v1/chat/completions"
        val apiKey = BuildConfig.CHAT_GPT_API_KEY
        val body = ChatRequest(
            model = Model.GPT_3_5.alias,
            messages = listOf(
                Message(
                    role = Role.User.alias,
                    content = message
                )
            ),
            temperature = 0.8f
        )

        client.post(url) {
            contentType(ContentType.Application.Json)
            setBody(body)
            headers {
                append(HttpHeaders.Authorization, "Bearer $apiKey")
            }
        }.body()
    }
}