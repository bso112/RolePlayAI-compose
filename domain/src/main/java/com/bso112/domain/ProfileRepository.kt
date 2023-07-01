package com.bso112.domain

import kotlinx.coroutines.flow.Flow

interface ProfileRepository : DataChangeNotifier {
    suspend fun changeUser(name: String, description: String, thumbnail: String)
    fun getUser(): Flow<Profile>
    suspend fun saveProfile(profile: Profile)
    fun getProfile(profileId: String): Flow<Profile>
    fun getProfiles(): Flow<List<Profile>>
    suspend fun deleteProfile(profile: Profile)
}