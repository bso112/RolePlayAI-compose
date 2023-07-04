package com.bso112.data.remote.datasource

import com.bso112.data.BuildConfig
import com.bso112.data.local.AppPreference
import com.bso112.data.remote.KtorClient
import com.bso112.data.remote.MessageApiModel
import com.bso112.data.remote.request.ChatRequest
import com.bso112.data.remote.request.TranslationRequest
import com.bso112.data.remote.response.ChatApiModel
import com.bso112.data.remote.response.TranslationResponse
import com.bso112.domain.Role
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType


class ChatRemoteDataSource(
    private val client: HttpClient = KtorClient.httpClient,
    private val appPreference: AppPreference
) {
    suspend fun translateWithGoogle(
        messages: List<String>,
        from: String,
        to: String
    ): TranslationResponse {
        val projectId = BuildConfig.GOOGLE_CLOUD_PROJECT_ID
        val apiKey = BuildConfig.GOOGLE_TRANSLATION_API_KEY
        val url = "https://translate.googleapis.com/v3beta1/projects/$projectId:translateText"
        val body = TranslationRequest(
            sourceLanguageCode = from,
            targetLanguageCode = to,
            contents = messages,
            mimeType = "text/plain"
        )

        return client.post(url) {
            contentType(ContentType.Application.Json)
            setBody(body)
            headers {
                append(HttpHeaders.Authorization, "Bearer $apiKey") //TODO Auth2 해서 엑세스 토큰 보내야 함.
            }
        }.body()
    }

    suspend fun translateWithGPT(message: String): String? {
        val url = "https://api.openai.com/v1/chat/completions"
        val apiKey = BuildConfig.CHAT_GPT_API_KEY
        val messageModel = MessageApiModel(
            content = "translate this to korean. if it's already korean, just send me a original korean text. Don't send me anything other then a result text: $message",
            role = Role.User.alias
        )
        val body = ChatRequest(
            model = appPreference.languageModel.getValue(),
            messages = listOf(messageModel),
            temperature = appPreference.temperature.getValue()
        )

        return client.post(url) {
            contentType(ContentType.Application.Json)
            setBody(body)
            headers {
                append(HttpHeaders.Authorization, "Bearer $apiKey")
            }
        }.body<ChatApiModel>().choices.takeIf { it.isNotEmpty() }?.first()?.message?.content
    }

    suspend fun sendChat(messages: List<MessageApiModel>): ChatApiModel {
        val url = "https://api.openai.com/v1/chat/completions"
        val apiKey = BuildConfig.CHAT_GPT_API_KEY
        val body = ChatRequest(
            model = appPreference.languageModel.getValue(),
            messages = messages,
            temperature = appPreference.temperature.getValue()
        )

        return client.post(url) {
            contentType(ContentType.Application.Json)
            setBody(body)
            headers {
                append(HttpHeaders.Authorization, "Bearer $apiKey")
            }
        }.body()
    }
}