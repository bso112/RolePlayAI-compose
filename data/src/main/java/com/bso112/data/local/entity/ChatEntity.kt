package com.bso112.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bso112.domain.Chat
import com.bso112.domain.Role

@Entity
data class ChatEntity(
    @PrimaryKey
    val id: String,
    val logId: String,
    val profileId: String,
    val name: String,
    val thumbnail: String,
    val message: String,
    val role: String,
    val createdAt: Long
)

fun ChatEntity.toDomain() =
    Chat(
        id = id,
        profileId = profileId,
        thumbnail = thumbnail,
        name = name,
        message = message,
        logId = logId,
        role = Role.fromAlias(role),
        createdAt = createdAt,
    )

fun Chat.toEntity() = ChatEntity(
    id = id,
    logId = logId,
    profileId = profileId,
    name = name,
    thumbnail = thumbnail,
    message = message,
    role = role.alias,
    createdAt = createdAt
)

fun ChatEntity.toChatLog(opponentId: String): ChatLogEntity {
    return ChatLogEntity(
        id = logId,
        name = name,
        thumbnail = thumbnail,
        previewMessage = message,
        opponentId = opponentId
    )
}