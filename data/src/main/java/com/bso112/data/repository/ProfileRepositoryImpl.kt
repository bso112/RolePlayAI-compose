package com.bso112.data.repository

import com.bso112.data.local.AppPreference
import com.bso112.data.local.datasource.ProfileLocalDataSource
import com.bso112.data.local.entity.ProfileEntity
import com.bso112.data.local.entity.toDomain
import com.bso112.data.local.entity.toEntity
import com.bso112.domain.DataChangedEvent
import com.bso112.domain.Profile
import com.bso112.domain.ProfileRepository
import com.bso112.domain.autoRefreshFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class ProfileRepositoryImpl(
    private val localDataSource: ProfileLocalDataSource,
    private val appPreference: AppPreference
) : ProfileRepository {

    object UserChanged : DataChangedEvent<Profile>
    object ProfileListChanged : DataChangedEvent<List<Profile>>

    private val _dataChangedEvent = MutableSharedFlow<DataChangedEvent<*>>()
    override val dataChangedEvent = _dataChangedEvent.asSharedFlow()

    override suspend fun changeUser(name: String, description: String, thumbnail: String) {
        saveProfile(
            appPreference.user.value.copy(
                name = name,
                description = description,
                thumbnail = thumbnail
            )
        )
        _dataChangedEvent.emit(UserChanged)
    }

    override fun getUser(): Flow<Profile> = autoRefreshFlow {
        appPreference.user.value
    }

    override suspend fun saveProfile(profile: Profile) {
        localDataSource.saveProfile(profile.toEntity())
        _dataChangedEvent.emit(ProfileListChanged)
    }

    override fun getProfile(profileId: String): Flow<Profile> = autoRefreshFlow {
        localDataSource.getProfileById(profileId)?.toDomain()!!
    }

    override fun getProfiles(): Flow<List<Profile>> = autoRefreshFlow {
        localDataSource.getAllProfile().map(ProfileEntity::toDomain)
    }

    override suspend fun deleteProfile(profile: Profile) {
        localDataSource.deleteProfile(profile.toEntity())
        _dataChangedEvent.emit(ProfileListChanged)
    }
}

