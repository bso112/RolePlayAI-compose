package com.bso112.domain

data class ChatMessage(
    val role: Role,
    val content: String
)


enum class Role(val alias: String) {
    System("system"),
    User("user"),
    Assistant("assistant"),
    Function("function");

    companion object {
        fun fromAlias(alias: String): Role {
            return Role.values().find { it.alias == alias } ?: error("cannot convert $alias to Role enum")
        }
    }
}