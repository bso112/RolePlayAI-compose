package com.bso112.roleplayai.android.util

import android.graphics.Typeface
import android.text.SpannedString
import android.text.style.StyleSpan
import androidx.core.text.toSpannable

object MessageParser {

    private val starRegex = "\\*([^*]*)\\*".toRegex()
    private val doubleQuotesRegex = "\"([^\"]*)\"".toRegex()
    private const val maxConsecutiveLineBreaks = 2
    private val maxConsecutiveLineBreaksRegex = "\n{${maxConsecutiveLineBreaks + 1},}".toRegex()

    fun fromMessage(message: String): CharSequence {
        return message
            .replace(doubleQuotesRegex) { "\n\n${it.value}\n\n" }
            .limitConsecutiveLineBreaks(2)
            .toSpannable().apply {
                val italicMatches = starRegex.findAll(this)
                val italicRanges = italicMatches.map { it.range }.toList()
                italicRanges.forEach { range ->
                    setSpan(
                        StyleSpan(Typeface.ITALIC),
                        range.first,
                        range.last,
                        SpannedString.SPAN_INCLUSIVE_INCLUSIVE
                    )
                }
            }.removeSurrounding("*")
    }

    private fun String.limitConsecutiveLineBreaks(maxConsecutiveLineBreaks: Int): String {
        return replace(maxConsecutiveLineBreaksRegex, "\n".repeat(maxConsecutiveLineBreaks))
    }

}