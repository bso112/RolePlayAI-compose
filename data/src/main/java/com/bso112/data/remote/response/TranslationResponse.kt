package com.bso112.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class TranslationResponse(
    val translatedText: List<String>
)