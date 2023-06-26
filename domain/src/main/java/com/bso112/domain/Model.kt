package com.bso112.domain

enum class Model(val alias: String, val maxTokenSize: Int) {
    GPT_3_5("gpt-3.5-turbo", 4096)
}

enum class Lang {
    KOR, EN
}


/**
 *  최대 토큰 수에 따라 최대 글자 수 근사치를 계산한다.
 */
fun Model.getMaxMessageLength(language: Lang): Int {
    return when (this) {
        Model.GPT_3_5 -> when (language) {
            Lang.KOR -> 4000
            Lang.EN -> 8000
        }
    }
}