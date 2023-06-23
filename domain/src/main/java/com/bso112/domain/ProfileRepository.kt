package com.bso112.domain

import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun saveProfile(profile: Profile): Flow<Unit>
    fun getProfile(profileId: String): Flow<Profile>
    fun getProfiles(): Flow<List<Profile>>
}