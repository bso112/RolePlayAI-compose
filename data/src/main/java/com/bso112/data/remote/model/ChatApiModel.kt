package com.bso112.data.remote.model

import com.bso112.data.remote.Message
import com.bso112.domain.Chat
import com.bso112.domain.Profile
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
    val message: Message,
    val finish_reason: String
)

@Serializable
data class Usage(
    val prompt_tokens: Int,
    val completion_tokens: Int,
    val total_tokens: Int
)


fun ChatApiModel.toDomain(speaker : Profile) =
    Chat(
        speaker = speaker,
        message = choices.firstOrNull()?.message?.content.orEmpty()
    )
