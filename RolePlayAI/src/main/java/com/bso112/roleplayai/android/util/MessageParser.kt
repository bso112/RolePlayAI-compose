package com.bso112.roleplayai.android.util

import android.graphics.Typeface
import android.text.SpannedString
import android.text.style.StyleSpan
import androidx.core.text.toSpannable

object MessageParser {

    private val boldRegex = "\\*([^*]*)\\*".toRegex()
    private val italicRegex = "\"([^\"]*)\"".toRegex()

    fun fromMessage(message: String): CharSequence {
        return message
            .replace(italicRegex) { "\n\n${it.value}\n\n" }
            .limitConsecutiveLineBreaks(2)
            .toSpannable().apply {
                val italicMatches = italicRegex.findAll(this)
                val italicRanges = italicMatches.map { it.range }.toList()
                logD("italicRanges: $italicRanges")
                italicRanges.forEach { range ->
                    setSpan(
                        StyleSpan(Typeface.ITALIC),
                        range.first,
                        range.last,
                        SpannedString.SPAN_INCLUSIVE_INCLUSIVE
                    )
                }

                val boldMatches = boldRegex.findAll(this)
                val boldRanges = boldMatches.map { it.range }.toList()
                logD("boldRanges: $boldRanges")
                boldRanges.forEach { range ->
                    setSpan(
                        StyleSpan(Typeface.BOLD),
                        range.first,
                        range.last,
                        SpannedString.SPAN_INCLUSIVE_INCLUSIVE
                    )
                }
            }
            .removeSurrounding("*")
    }

    private fun String.limitConsecutiveLineBreaks(maxConsecutiveLineBreaks: Int): String {
        val regexPattern = "\n{${maxConsecutiveLineBreaks + 1},}"
        return replace(Regex(regexPattern), "\n".repeat(maxConsecutiveLineBreaks))
    }

}