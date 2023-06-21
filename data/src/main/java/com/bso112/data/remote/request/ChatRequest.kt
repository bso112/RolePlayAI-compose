package com.bso112.data.remote.request

import com.bso112.data.remote.Message
import kotlinx.serialization.Serializable

enum class Model(val alias: String) {
    GPT_3_5("gpt-3.5-turbo")
}

@Serializable
data class ChatRequest(
    val model: String,
    val messages: List<Message>,
    val temperature: Float
)
