package com.bso112.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val role: String,
    val content: String
)

enum class Role(val alias: String) {
    System("system"),
    User("user"),
    Assistant("assistant"),
    Function("function")
}

