package com.bso112.domain

data class Chat(
    val id : String,
    val profileId: String,
    val thumbnail: String,
    val name: String,
    val message: String
)
