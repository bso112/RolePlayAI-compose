package com.bso112.data.local

import android.content.Context
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
import com.bso112.data.logD
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
                if (appPreference.userId.getValue() == null) {
                    val userId = UUID.randomUUID().toString()
                    val sql = "INSERT INTO ProfileEntity (id, name, thumbnail, description) VALUES ('${userId}', '유저', '', '')"
                    db.execSQL(sql)
                    logD(sql)
                    appPreference.userId.setValue(userId)
                }
            }
        }
    }).build()
