package com.bso112.data.repository

import com.bso112.data.local.datasource.ProfileLocalDataSource
import com.bso112.data.local.entity.ProfileEntity
import com.bso112.data.local.entity.toDomain
import com.bso112.data.local.entity.toEntity
import com.bso112.domain.Profile
import com.bso112.domain.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProfileRepositoryImpl(
    private val localDataSource: ProfileLocalDataSource
) : ProfileRepository {
    override fun saveProfile(profile: Profile): Flow<Unit> = flow {
        localDataSource.saveProfile(profile.toEntity())
        emit(Unit)
    }

    override fun getProfile(profileId: String): Flow<Profile> = flow {
        emit(localDataSource.getProfileById(profileId).toDomain())
    }

    override fun getProfiles(): Flow<List<Profile>> = flow {
        emit(localDataSource.getAllProfile().map(ProfileEntity::toDomain))
    }
}
