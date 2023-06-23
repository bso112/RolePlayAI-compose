package com.bso112.data.local.datasource

import androidx.room.withTransaction
import com.bso112.data.local.AppDataBase
import com.bso112.data.local.dao.ProfileDao
import com.bso112.data.local.entity.ProfileEntity

class ProfileLocalDataSource(
    private val db: AppDataBase
) {
    private val profileDao: ProfileDao = db.profileDao()

    suspend fun getAllProfile(): List<ProfileEntity> {
        return db.withTransaction {
            profileDao.getAllProfile()
        }
    }

    suspend fun getProfileById(profileId: String): ProfileEntity {
        return db.withTransaction {
            profileDao.getProfileById(profileId)
        }
    }

    suspend fun saveProfile(profile: ProfileEntity) {
        db.withTransaction {
            profileDao.saveProfile(profile)
        }
    }
}