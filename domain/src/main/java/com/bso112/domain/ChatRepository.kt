package com.bso112.domain

import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun sendChat(speaker: Profile, message: String): Result<Chat>
    suspend fun getAllChat(logId: String): Result<List<Chat>>
    suspend fun saveChatList(logId: String, list: List<Chat>): Result<Unit>
    fun getChatLog(): Flow<List<ChatLog>>
}