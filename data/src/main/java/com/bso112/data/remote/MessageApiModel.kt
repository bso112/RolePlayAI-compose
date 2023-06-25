package com.bso112.data.remote

import com.bso112.domain.ChatMessage
import kotlinx.serialization.Serializable

@Serializable
data class MessageApiModel(
    val role: String,
    val content: String
)

fun ChatMessage.toApiModel() = MessageApiModel(
    role = role.alias,
    content = content
)


