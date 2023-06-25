package com.bso112.domain

import kotlinx.coroutines.flow.Flow

interface ChatRepository : DataChangeNotifier {

    suspend fun saveChat(chat: Chat)
    fun sendChat(speaker: Profile, messages: List<ChatMessage>, logId: String): Flow<Chat>
    fun getAllChat(logId: String): Flow<List<Chat>>
    fun getChatLog(): Flow<List<ChatLog>>
}