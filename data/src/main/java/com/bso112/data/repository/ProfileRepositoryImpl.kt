package com.bso112.data.repository

import com.bso112.data.local.AppPreference
import com.bso112.data.local.datasource.ProfileLocalDataSource
import com.bso112.data.local.entity.ProfileEntity
import com.bso112.data.local.entity.toDomain
import com.bso112.data.local.entity.toEntity
import com.bso112.domain.Profile
import com.bso112.domain.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge

class ProfileRepositoryImpl(
    private val localDataSource: ProfileLocalDataSource,
    private val appPreference: AppPreference
) : ProfileRepository {

    private val _dataChangedEvent = MutableSharedFlow<ProfileRepository.DataChangedEvent<*>>()
    override val dataChangedEvent = _dataChangedEvent.asSharedFlow()

    override suspend fun saveUser(profile: Profile) {
        saveProfile(profile)
        appPreference.saveUserId(profile.id)
        _dataChangedEvent.emit(ProfileRepository.DataChangedEvent.User)
    }

    override fun getUser(defaultUser: Profile): Flow<Profile> = autoRefreshFlow {
        val userId = appPreference.userId.first()
        return@autoRefreshFlow if (userId.isEmpty()) {
            //do not call ProfileRepositoryImpl.saveUser().
            //it cause recursive function call because it emit DataChangedEvent event
            defaultUser.also {
                saveProfile(it)
                appPreference.saveUserId(it.id)
            }
        } else {
            localDataSource.getProfileById(userId).toDomain()
        }
    }

    override suspend fun saveProfile(profile: Profile) {
        localDataSource.saveProfile(profile.toEntity())
        _dataChangedEvent.emit(ProfileRepository.DataChangedEvent.ProfileList)
    }

    override fun getProfile(profileId: String): Flow<Profile> = flow {
        emit(localDataSource.getProfileById(profileId).toDomain())
    }

    override fun getProfiles(): Flow<List<Profile>> = autoRefreshFlow {
        localDataSource.getAllProfile().map(ProfileEntity::toDomain)
    }


    private inline fun <reified T> autoRefreshFlow(
        crossinline block: suspend () -> T
    ) = merge(
        flow { emit(block()) },
        dataChangedEvent.filterIsInstance<ProfileRepository.DataChangedEvent<T>>().map {
            block()
        })

}

