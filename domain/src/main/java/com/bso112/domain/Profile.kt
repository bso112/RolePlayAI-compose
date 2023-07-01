package com.bso112.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class Profile(
    val id: String,
    val thumbnail: String,
    val name: String,
    val description: String,
    val firstMessage : String,
) : Parcelable {
    companion object {
        val Empty = Profile(
            id = UUID.randomUUID().toString(),
            thumbnail = "",
            name = "",
            description = "",
            firstMessage = ""
        )
    }
}

/**
 * Chat에 Profile을 포함하려 했으나, ChatEntity -> Chat 객체를 만들때가 문제다.
 * ChatEntity에는 Profile에 대한 모든 정보를 담지 않기에 Profile 객체를 만들 수 업다.
 * 따라서 Chat은 Profile의 일부 속성만 가지고 있다.
 */
fun Profile.createChat(message: String, logId: String, role : Role) = Chat(
    id = UUID.randomUUID().toString(),
    logId = logId,
    profileId = id,
    thumbnail = thumbnail,
    name = name,
    message = message,
    role = role
)
