package com.bso112.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bso112.data.local.entity.ChatEntity

@Dao
interface ChatDao {
    @Query("select * from ChatEntity where logId = :logId")
    suspend fun getAllChatById(logId : String): List<ChatEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<ChatEntity>)
}