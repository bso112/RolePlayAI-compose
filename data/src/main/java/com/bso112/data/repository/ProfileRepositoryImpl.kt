package com.bso112.data.repository

import com.bso112.data.alsoSuspend
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
import kotlinx.coroutines.flow.flow

class ProfileRepositoryImpl(
    private val localDataSource: ProfileLocalDataSource,
    private val appPreference: AppPreference
) : ProfileRepository {

    object UserChanged : DataChangedEvent<Profile>
    object ProfileListChanged : DataChangedEvent<List<Profile>>

    private val _dataChangedEvent = MutableSharedFlow<DataChangedEvent<*>>()
    override val dataChangedEvent = _dataChangedEvent.asSharedFlow()

    override suspend fun saveUser(profile: Profile) {
        saveProfile(profile)
        appPreference.userId.setValue(profile.id)
        _dataChangedEvent.emit(UserChanged)
    }

    override fun getUser(defaultUser: Profile): Flow<Profile> = autoRefreshFlow {
        val userId = appPreference.userId.getValue().orEmpty()
        localDataSource.getProfileById(userId)?.toDomain() ?: defaultUser
    }

    override suspend fun saveProfile(profile: Profile) {
        localDataSource.saveProfile(profile.toEntity())
        _dataChangedEvent.emit(ProfileListChanged)
    }

    override fun getProfile(profileId: String): Flow<Profile> = flow {
        localDataSource.getProfileById(profileId)?.toDomain()?.alsoSuspend(::emit)
    }

    override fun getProfiles(): Flow<List<Profile>> = autoRefreshFlow {
        localDataSource.getAllProfile().map(ProfileEntity::toDomain)
    }

    override suspend fun deleteProfile(profile: Profile) {
        localDataSource.deleteProfile(profile.toEntity())
        _dataChangedEvent.emit(ProfileListChanged)
    }
}

