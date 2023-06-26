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
        return@autoRefreshFlow if (userId.isEmpty()) {
            //do not call ProfileRepositoryImpl.saveUser().
            //it cause recursive function call because it emit DataChangedEvent event
            defaultUser.also {
                saveProfile(it)
                appPreference.userId.setValue(it.id)
            }
        } else {
            localDataSource.getProfileById(userId).toDomain()
        }
    }

    override suspend fun saveProfile(profile: Profile) {
        localDataSource.saveProfile(profile.toEntity())
        _dataChangedEvent.emit(ProfileListChanged)
    }

    override fun getProfile(profileId: String): Flow<Profile> = flow {
        emit(localDataSource.getProfileById(profileId).toDomain())
    }

    override fun getProfiles(): Flow<List<Profile>> = autoRefreshFlow {
        localDataSource.getAllProfile().map(ProfileEntity::toDomain)
    }
}

