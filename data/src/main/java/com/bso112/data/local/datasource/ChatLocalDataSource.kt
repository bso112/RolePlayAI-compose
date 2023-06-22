package com.bso112.data.local.datasource

import androidx.room.withTransaction
import com.bso112.data.local.AppDataBase
import com.bso112.data.local.entity.ChatEntity
import com.bso112.data.local.entity.ChatLogEntity
import com.bso112.data.local.entity.toChatLog

class ChatLocalDataSource(
    private val db: AppDataBase
) {
    private val chatDao = db.chatDao()
    private val chatLogDao = db.chatLogDao()

    suspend fun getAllChat(logId: String): List<ChatEntity> {
        return db.withTransaction {
            chatDao.getAllChatById(logId)
        }
    }

    suspend fun saveChatList(list: List<ChatEntity>, logId: String) {
        val chatLog = list.toChatLog(logId) ?: error("chat list cannot be empty")
        db.withTransaction {
            chatDao.insertAll(list)
            chatLogDao.insert(chatLog)
        }
    }

    suspend fun getChatLog(): List<ChatLogEntity> {
        return db.withTransaction {
            chatLogDao.getAll()
        }
    }
}