package com.bso112.data.local.datasource

import androidx.room.withTransaction
import com.bso112.data.local.AppDataBase
import com.bso112.data.local.entity.ChatEntity
import com.bso112.data.local.entity.ChatLogEntity

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

    suspend fun saveChatList(chat: List<ChatEntity>) {
        db.withTransaction {
            chatDao.insertAll(chat)
        }
    }

    suspend fun saveChatLog(chatLog: ChatLogEntity) {
        db.withTransaction {
            chatLogDao.insert(chatLog)
        }
    }

    suspend fun getChatLog(): List<ChatLogEntity> {
        return db.withTransaction {
            chatLogDao.getAll()
        }
    }

    suspend fun getChatLogByProfileId(profileId: String): List<ChatLogEntity> {
        return db.withTransaction {
            chatLogDao.getByProfileId(profileId)
        }
    }

    suspend fun deleteChatLog(chatLog: ChatLogEntity) {
        return db.withTransaction {
            chatLogDao.delete(chatLog)
        }
    }

    suspend fun deleteChatLogList(chatLogList : List<ChatLogEntity>){
        return db.withTransaction {
            chatLogDao.deleteList(chatLogList)
        }
    }

    suspend fun deleteByProfileId(profileId : String){
        return db.withTransaction {
            chatLogDao.deleteByProfileId(profileId)
        }
    }
}