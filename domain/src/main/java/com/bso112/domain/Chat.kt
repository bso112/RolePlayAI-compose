package com.bso112.domain

data class Chat(
    val id: String,
    val logId: String,
    val profileId: String,
    val thumbnail: String,
    val name: String,
    val message: String,
    val role: Role
)

fun Chat.toMessage() = ChatMessage(
    role = role,
    content = message
)
