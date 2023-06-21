package com.bso112.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bso112.domain.Chat
import com.bso112.domain.Profile

@Entity
data class ChatEntity(
    val logId: String,
    val name: String,
    val thumbnail: String,
    val message: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}

fun ChatEntity.toDomain() =
    Chat(speaker = Profile(id = id, thumbnail = thumbnail, name = name), message = message)

fun Chat.toEntity(logId: String) = ChatEntity(
    logId = logId,
    name = speaker.name,
    thumbnail = speaker.thumbnail,
    message = message
)