package com.bso112.domain

import kotlinx.coroutines.flow.Flow

interface ChatRepository : DataChangeNotifier {

    fun translateWithGoogle(
        message: String,
        sourceLanguageCode: LanguageCode,
        targetLanguageCode: LanguageCode,
    ): Flow<String>

    fun translateWithGPT(
        message : String
    ) : Flow<String>


    suspend fun saveChatList(chatList: List<Chat>,  opponentId : String)
    fun sendChat(speaker: Profile, messages: List<Chat>, logId: String): Flow<Chat>
    fun getAllChat(logId: String): Flow<List<Chat>>
    fun getAllChatLog(): Flow<List<ChatLog>>
    fun getChatLogByProfileId(profileId: String): Flow<List<ChatLog>>
    suspend fun deleteChatLog(chatLog: ChatLog)
    suspend fun deleteChatLogByProfileId(profileId: String)
}