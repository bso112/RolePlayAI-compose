package com.bso112.data.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase.CONFLICT_IGNORE
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bso112.data.local.dao.ChatDao
import com.bso112.data.local.dao.ChatLogDao
import com.bso112.data.local.dao.ProfileDao
import com.bso112.data.local.entity.ChatEntity
import com.bso112.data.local.entity.ChatLogEntity
import com.bso112.data.local.entity.ProfileEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

@Database(entities = [ChatEntity::class, ProfileEntity::class, ChatLogEntity::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun chatDao(): ChatDao
    abstract fun chatLogDao(): ChatLogDao
    abstract fun profileDao(): ProfileDao
}

fun createDataBase(applicationContext: Context, appPreference: AppPreference): AppDataBase =
    Room.databaseBuilder(
        applicationContext,
        AppDataBase::class.java, "RolePlayAI"
    ).addCallback(object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            CoroutineScope(Dispatchers.IO).launch {
                val userId = appPreference.userId.getValue() ?: UUID.randomUUID().toString()
                db.insert("ProfileEntity", CONFLICT_IGNORE, ContentValues().apply {
                    put("id", userId)
                    put("name", "유저")
                    put("thumbnail", "")
                    put("description", "")
                    put("firstMessage", "")
                })
                appPreference.userId.setValue(userId)
            }
        }
    }).build()
