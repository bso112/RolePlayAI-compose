package com.bso112.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.bso112.data.local.entity.ProfileEntity

@Dao
interface ProfileDao {
    @Query("select * from ProfileEntity")
    suspend fun getAllChat(): List<ProfileEntity>
}