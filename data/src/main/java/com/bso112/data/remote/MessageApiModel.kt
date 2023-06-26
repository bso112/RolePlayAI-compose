package com.bso112.data.remote

import com.bso112.domain.Chat
import kotlinx.serialization.Serializable

@Serializable
data class MessageApiModel(
    val role: String,
    val content: String
)

fun Chat.toApiModel() = MessageApiModel(
    role = role.alias,
    content = message
)


