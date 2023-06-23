package com.bso112.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bso112.domain.Profile

@Entity
data class ProfileEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val thumbnail: String,
    val description: String
)

fun ProfileEntity.toDomain() =
    Profile(id = id, name = name, thumbnail = thumbnail, description = description)

fun Profile.toEntity() =
    ProfileEntity(id = id, name = name, thumbnail = thumbnail, description = description)