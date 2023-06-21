package com.bso112.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bso112.domain.Profile

@Entity
data class ProfileEntity(
    val name: String,
    val thumbnail: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}

fun ProfileEntity.toDomain() = Profile(id = id, name = name, thumbnail = thumbnail)