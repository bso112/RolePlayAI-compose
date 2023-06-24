package com.bso112.domain

import kotlinx.coroutines.flow.Flow

interface ProfileRepository : DataChangeNotifier {
    suspend fun saveUser(profile: Profile)
    fun getUser(defaultUser: Profile): Flow<Profile>
    suspend fun saveProfile(profile: Profile)
    fun getProfile(profileId: String): Flow<Profile>
    fun getProfiles(): Flow<List<Profile>>
}