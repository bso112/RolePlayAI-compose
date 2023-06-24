package com.bso112.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface ProfileRepository {

    val dataChangedEvent: SharedFlow<DataChangedEvent<*>>
    suspend fun saveUser(profile: Profile)
    fun getUser(defaultUser: Profile): Flow<Profile>
    suspend fun saveProfile(profile: Profile)
    fun getProfile(profileId: String): Flow<Profile>
    fun getProfiles(): Flow<List<Profile>>


    sealed interface DataChangedEvent<T> {
        object User : DataChangedEvent<User>
        object ProfileList : DataChangedEvent<List<Profile>>
    }
}