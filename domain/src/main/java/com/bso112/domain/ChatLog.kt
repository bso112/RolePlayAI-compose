package com.bso112.domain

data class ChatLog(
    val id: String,
    val profileId: String,
    val name: String,
    val thumbnail: String,
    val previewMessage: String
)