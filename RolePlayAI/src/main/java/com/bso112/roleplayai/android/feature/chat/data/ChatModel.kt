package com.bso112.roleplayai.android.feature.chat.data

import com.bso112.domain.Chat
import com.bso112.domain.Role

interface ChatModel {
    data class UIModelChat(
        val chat: Chat
    ) : ChatModel

    data class PromptChat(
        val message: String,
        val role: Role
    ) : ChatModel

    data class SystemChat(
        val name: String,
        val thumbnail: String,
        val message: String,
        val logId: String,
    ) : ChatModel
}