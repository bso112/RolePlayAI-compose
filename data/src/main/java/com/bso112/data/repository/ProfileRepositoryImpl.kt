package com.bso112.data.repository

import com.bso112.data.local.entity.ProfileEntity
import com.bso112.data.local.entity.toDomain
import com.bso112.domain.Profile
import com.bso112.domain.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProfileRepositoryImpl : ProfileRepository {
    override fun getProfiles(): Flow<List<Profile>> = flow {
        emit(fakeProfileList.map(ProfileEntity::toDomain))
    }
}

val fakeProfileList = List(size = 20) {
    ProfileEntity(
        name = "Bot $it",
        thumbnail = ""
    )
}