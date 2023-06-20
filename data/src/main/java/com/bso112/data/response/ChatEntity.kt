package com.bso112.data.response

import com.bso112.data.Message
import kotlinx.serialization.Serializable

@Serializable
data class ChatEntity(
    val id: String,
    val `object`: String,
    val created: Long,
    val model : String,
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

