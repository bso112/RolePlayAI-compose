package com.bso112.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bso112.data.local.entity.ChatLogEntity

@Dao
interface ChatLogDao {

    @Query("select * from ChatLogEntity")
    suspend fun getAll(): List<ChatLogEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(chatLog: ChatLogEntity)

    @Delete
    suspend fun delete(chatLog: ChatLogEntity)

    @Delete
    suspend fun deleteList(chatLogList: List<ChatLogEntity>)

    @Query("delete from ChatLogEntity where opponentId = :profileId")
    suspend fun deleteByProfileId(profileId: String)

    @Query("select * from ChatLogEntity where opponentId = :profileId")
    suspend fun getByProfileId(profileId: String): List<ChatLogEntity>

}