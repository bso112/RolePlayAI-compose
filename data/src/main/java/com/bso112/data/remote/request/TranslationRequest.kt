package com.bso112.data.remote.request

import kotlinx.serialization.Serializable


@Serializable
data class TranslationRequest(
    val sourceLanguageCode: String,
    val targetLanguageCode: String,
    val contents: List<String>,
    val mimeType: String
)