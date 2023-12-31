package com.bso112.data.remote.response

import com.bso112.data.remote.MessageApiModel
import com.bso112.domain.Profile
import com.bso112.domain.Role
import com.bso112.domain.createChat
import kotlinx.serialization.Serializable

@Serializable
data class ChatApiModel(
    val id: String,
    val `object`: String,
    val created: Long,
    val model: String,
    val choices: List<Choice>,
    val usage: Usage
)

@Serializable
data class Choice(
    val index: Int,
    val message: MessageApiModel,
    val finish_reason: String
)

@Serializable
data class Usage(
    val prompt_tokens: Int,
    val completion_tokens: Int,
    val total_tokens: Int
)


fun ChatApiModel.toDomain(speaker: Profile, logId : String) =
    speaker.createChat(message = choices.firstOrNull()?.message?.content.orEmpty(), logId = logId, role = Role.Assistant)
