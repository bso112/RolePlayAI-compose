package com.bso112.domain

data class ChatLog(
    val id: String,
    val opponentId: String,
    val name: String,
    val thumbnail: String,
    val previewMessage: String
)

/**
 * @param opponentId 대화 상대방의 id
 */
fun Chat.toChatLog(opponentId: String = profileId) = ChatLog(
    id = logId,
    opponentId = opponentId,
    name = name,
    thumbnail = thumbnail,
    previewMessage = message
)
