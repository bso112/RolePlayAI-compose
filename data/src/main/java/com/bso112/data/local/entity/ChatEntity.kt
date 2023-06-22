package com.bso112.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bso112.domain.Chat

@Entity
data class ChatEntity(
    @PrimaryKey
    val id: String,
    val logId: String,
    val profileId: String,
    val name: String,
    val thumbnail: String,
    val message: String
)

fun ChatEntity.toDomain() =
    Chat(id = id, profileId = profileId, thumbnail = thumbnail, name = name, message = message)

fun Chat.toEntity(logId: String) = ChatEntity(
    id = id,
    logId = logId,
    profileId = profileId,
    name = name,
    thumbnail = thumbnail,
    message = message
)