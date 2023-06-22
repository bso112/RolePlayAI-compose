package com.bso112.data.repository

import com.bso112.data.local.entity.ProfileEntity
import com.bso112.data.local.entity.toDomain
import com.bso112.domain.Profile
import com.bso112.domain.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.UUID

class ProfileRepositoryImpl : ProfileRepository {
    override fun getProfile(profileId: String): Flow<Profile> = flow {
        emit(fakeProfile.toDomain())
    }

    override fun getProfiles(): Flow<List<Profile>> = flow {
        emit(fakeProfileList.map(ProfileEntity::toDomain))
    }
}

val fakeProfile = ProfileEntity(
    id = UUID.randomUUID().toString(),
    name = "Bot",
    thumbnail = ""
)

val fakeProfileList = List(size = 20) {
    ProfileEntity(
        id = UUID.randomUUID().toString(),
        name = "Bot $it",
        thumbnail = ""
    )
}