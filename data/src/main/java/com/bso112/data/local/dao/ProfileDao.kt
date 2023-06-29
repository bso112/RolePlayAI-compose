package com.bso112.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bso112.data.local.entity.ProfileEntity

@Dao
interface ProfileDao {
    @Query("select * from ProfileEntity")
    suspend fun getAllProfile(): List<ProfileEntity>

    @Query("select * from ProfileEntity where id = :profileId limit 1")
    suspend fun getProfileById(profileId: String): ProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProfile(profileEntity: ProfileEntity)

    @Delete
    suspend fun deleteProfile(profileEntity: ProfileEntity)
}