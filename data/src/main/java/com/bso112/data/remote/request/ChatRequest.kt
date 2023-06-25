package com.bso112.data.remote.request

import com.bso112.data.remote.MessageApiModel
import kotlinx.serialization.Serializable

enum class Model(val alias: String, val maxToken : Int) {
    GPT_3_5("gpt-3.5-turbo", 2048)
}

@Serializable
data class ChatRequest(
    val model: String,
    val messages: List<MessageApiModel>,
    val temperature: Float
)
