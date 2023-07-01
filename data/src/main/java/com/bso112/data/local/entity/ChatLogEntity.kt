package com.bso112.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bso112.domain.ChatLog

@Entity
data class ChatLogEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val opponentId: String,
    val thumbnail: String,
    val previewMessage: String
)

fun ChatLogEntity.toDomain() = ChatLog(
    id = id,
    name = name,
    opponentId = opponentId,
    thumbnail = thumbnail,
    previewMessage = previewMessage
)

fun ChatLog.toEntity() = ChatLogEntity(
    id = id,
    name = name,
    opponentId = opponentId,
    thumbnail = thumbnail,
    previewMessage = previewMessage
)