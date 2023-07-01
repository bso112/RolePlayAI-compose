package com.bso112.domain

import kotlinx.coroutines.flow.Flow

interface ChatRepository : DataChangeNotifier {

    suspend fun saveChat(chat: Chat)

    suspend fun saveChatLog(chatLog: ChatLog)
    fun sendChat(speaker: Profile, messages: List<Chat>, logId: String): Flow<Chat>
    fun getAllChat(logId: String): Flow<List<Chat>>
    fun getChatLog(): Flow<List<ChatLog>>
    suspend fun deleteChatLog(chatLog: ChatLog)
    suspend fun deleteChatLogByProfileId(profileId : String)
}