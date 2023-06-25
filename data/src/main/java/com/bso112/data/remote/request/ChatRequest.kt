package com.bso112.data.remote.request

import com.bso112.data.remote.MessageApiModel
import kotlinx.serialization.Serializable

@Serializable
data class ChatRequest(
    val model: String,
    val messages: List<MessageApiModel>,
    val temperature: Float
)
