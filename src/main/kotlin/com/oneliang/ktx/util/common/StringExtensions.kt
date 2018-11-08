package com.oneliang.ktx.util.common

import com.oneliang.ktx.Constants
import java.text.SimpleDateFormat
import java.util.*

fun String.toIntSafely(defaultValue: Int = 0): Int {
    return try {
        this.toInt()
    } catch (e: Exception) {
        defaultValue
    }
}

fun String.toLongSafely(defaultValue: Long = 0): Long {
    return try {
        this.toLong()
    } catch (e: Exception) {
        defaultValue
    }
}

fun String.toBooleanSafely(defaultValue: Boolean = false): Boolean {
    return try {
        this.toBoolean()
    } catch (e: Exception) {
        defaultValue
    }
}

fun String.toUtilDate(format: String = Constants.Time.YEAR_MONTH_DAY_HOUR_MINUTE_SECOND, locale: Locale = Locale.getDefault()): Date {
    val simpleDateFormat = SimpleDateFormat(format, locale)
    return simpleDateFormat.parse(this)
}

private val METCH_PATTERN_REGEX = "[\\*]+".toRegex()
private const val METCH_PATTERN = Constants.Symbol.WILDCARD
private const val METCH_PATTERN_REPLACEMENT = "[\\\\S|\\\\s]*"

fun CharSequence.matchPattern(pattern: String): Boolean {
    if (pattern.indexOf(METCH_PATTERN) >= 0) {
        val matchPattern = Constants.Symbol.XOR + pattern.replace(METCH_PATTERN_REGEX, METCH_PATTERN_REPLACEMENT) + Constants.Symbol.DOLLAR
        return this.matches(matchPattern.toRegex())
    } else {
        if (this == pattern) {
            return true
        }
    }
    return false
}

