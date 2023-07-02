package com.bso112.domain

/**
 * @param profileId: id of speaker's profile
 * @param logId: id of chat log
 * @param onlyForUi: if True, this chat is only for UI. It will not be send to server.
 */
data class Chat(
    val id: String,
    val logId: String,
    val profileId: String,
    val thumbnail: String,
    val name: String,
    val message: String,
    val role: Role,
    val onlyForUi: Boolean = false
)

fun String.asPrompt(userName: String, charName: String) =
    replace("{{user}}", userName).replace("{{char}}", charName)