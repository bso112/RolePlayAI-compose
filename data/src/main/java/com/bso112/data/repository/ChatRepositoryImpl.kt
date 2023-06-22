package com.bso112.data.repository

import com.bso112.data.alsoSuspend
import com.bso112.data.local.datasource.ChatLocalDataSource
import com.bso112.data.local.entity.ChatEntity
import com.bso112.data.local.entity.ChatLogEntity
import com.bso112.data.local.entity.toDomain
import com.bso112.data.local.entity.toEntity
import com.bso112.data.remote.datasource.ChatRemoteDataSource
import com.bso112.data.remote.model.toDomain
import com.bso112.data.result
import com.bso112.domain.Chat
import com.bso112.domain.ChatLog
import com.bso112.domain.ChatRepository
import com.bso112.domain.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ChatRepositoryImpl(
    private val chatRemoteDataSource: ChatRemoteDataSource,
    private val chatLocalDataSource: ChatLocalDataSource
) : ChatRepository {
    override suspend fun sendChat(
        speaker: Profile,
        message: String
    ): Result<Chat> = result {
        chatRemoteDataSource.sendChat(message).toDomain(speaker)
    }

    override suspend fun getAllChat(
        logId: String
    ): Result<List<Chat>> = result {
        chatLocalDataSource.getAllChat(logId).map(ChatEntity::toDomain)
    }

    override suspend fun saveChatLog(
        logId: String,
        list: List<Chat>
    ): Result<Unit> = result {
        chatLocalDataSource.saveChatList(list.map { it.toEntity(logId) }, logId)
    }

    override fun getChatLog(): Flow<List<ChatLog>> = flow {
        chatLocalDataSource.getChatLog().map(ChatLogEntity::toDomain).alsoSuspend(::emit)
    }
}

