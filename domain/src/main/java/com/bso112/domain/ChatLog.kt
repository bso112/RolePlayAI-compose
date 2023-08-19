package com.bso112.domain

data class ChatLog(
    val id: String,
    val opponentId: String,
    val name: String,
    val opponentName: String,
    val thumbnail: String,
    val previewMessage: String,
    val modifiedAt: Long,
    val alias: String
)

/**
 * @param opponentId 대화 상대방의 id
 * @param opponentName 대화 상대방의 이름
 */
fun Chat.toChatLog(opponentName: String, opponentId: String = profileId) = ChatLog(
    id = logId,
    opponentName = opponentName,
    opponentId = opponentId,
    name = name,
    thumbnail = thumbnail,
    previewMessage = message,
    modifiedAt = System.currentTimeMillis(),
    alias = message.take(20)
)
