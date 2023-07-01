package com.bso112.domain

import java.util.UUID

data class Chat(
    val id: String,
    val logId: String,
    val profileId: String,
    val thumbnail: String,
    val name: String,
    val message: String,
    val role: Role
)

fun String.toChat(userName: String, charName: String, role : Role) = Chat(
    id = UUID.randomUUID().toString(),
    logId = UUID.randomUUID().toString(),
    profileId = UUID.randomUUID().toString(),
    thumbnail = "",
    name = "",
    message = this.replace("{{user}}", userName).replace("{{char}}", charName),
    role = role
)
