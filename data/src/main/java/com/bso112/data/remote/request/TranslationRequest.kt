package com.bso112.data.remote.request


data class TranslationRequest(
    val sourceLanguageCode: String,
    val targetLanguageCode: String,
    val contents: List<String>,
    val mimeType: String
)