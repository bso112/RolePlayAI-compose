package com.bso112.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bso112.data.local.dao.ChatDao
import com.bso112.data.local.entity.ChatEntity
import com.bso112.data.local.entity.ProfileEntity

@Database(entities = [ChatEntity::class, ProfileEntity::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun chatDao(): ChatDao
}

fun createDataBase(applicationContext: Context): AppDataBase = Room.databaseBuilder(
    applicationContext,
    AppDataBase::class.java, "RolePlayAI"
).build()