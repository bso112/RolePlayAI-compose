package com.bso112.roleplayai.android.feature.chat.data

import com.bso112.data.response.CharacterEntity

data class Character(
    val thumbnail: String,
    val name: String
)

fun CharacterEntity.toUIModel() = Character(
    thumbnail = thumbnail,
    name = name
)