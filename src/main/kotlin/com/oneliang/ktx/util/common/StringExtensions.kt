package com.oneliang.ktx.util.common

import com.oneliang.ktx.Constants
import java.text.SimpleDateFormat
import java.util.*

fun String.toIntSafely(defaultValue: Int = 0): Int = try {
    java.lang.Integer.decode(this).toString().toInt()
} catch (e: Exception) {
    defaultValue
}

fun String.toLongSafely(defaultValue: Long = 0): Long = try {
    java.lang.Long.decode(this)
} catch (e: Exception) {
    defaultValue
}

fun String.toFloatSafely(defaultValue: Float = 0f): Float = try {
    this.toFloat()
} catch (e: Exception) {
    defaultValue
}

fun String.toLongSafely(defaultValue: Double = 0.0): Double = try {
    this.toDouble()
} catch (e: Exception) {
    defaultValue
}

fun String.toBooleanSafely(defaultValue: Boolean = false): Boolean = try {
    this.toBoolean()
} catch (e: Exception) {
    defaultValue
}

fun String.hexStringToByteArray(): ByteArray = ByteArray(this.length / 2) { this.substring(it * 2, it * 2 + 2).toInt(16).toByte() }

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

