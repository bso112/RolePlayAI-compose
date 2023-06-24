package com.bso112.data.repository

import com.bso112.data.alsoSuspend
import com.bso112.data.local.datasource.ChatLocalDataSource
import com.bso112.data.local.entity.ChatEntity
import com.bso112.data.local.entity.ChatLogEntity
import com.bso112.data.local.entity.toDomain
import com.bso112.data.local.entity.toEntity
import com.bso112.data.remote.datasource.ChatRemoteDataSource
import com.bso112.data.remote.model.toDomain
import com.bso112.domain.Chat
import com.bso112.domain.ChatLog
import com.bso112.domain.ChatRepository
import com.bso112.domain.DataChangedEvent
import com.bso112.domain.Profile
import com.bso112.domain.autoRefreshFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow

class ChatRepositoryImpl(
    private val chatRemoteDataSource: ChatRemoteDataSource,
    private val chatLocalDataSource: ChatLocalDataSource,
) : ChatRepository {
    private val _dataChangedEvent = MutableSharedFlow<DataChangedEvent<*>>()
    override val dataChangedEvent: SharedFlow<DataChangedEvent<*>> =
        _dataChangedEvent.asSharedFlow()

    object ChatListChanged : DataChangedEvent<Chat>

    override suspend fun saveChat(
        chat: Chat
    ) {
        chatLocalDataSource.saveChat(chat.toEntity())
        _dataChangedEvent.emit(ChatListChanged)
    }

    override fun sendChat(
        speaker: Profile,
        message: String,
        logId: String
    ): Flow<Chat> = flow {
        chatRemoteDataSource.sendChat(message).toDomain(speaker, logId).alsoSuspend(::emit)
    }

    override fun getAllChat(
        logId: String
    ): Flow<List<Chat>> = autoRefreshFlow {
        chatLocalDataSource.getAllChat(logId).map(ChatEntity::toDomain)
    }

    override fun getChatLog(): Flow<List<ChatLog>> = flow {
        chatLocalDataSource.getChatLog().map(ChatLogEntity::toDomain).alsoSuspend(::emit)
    }
}

