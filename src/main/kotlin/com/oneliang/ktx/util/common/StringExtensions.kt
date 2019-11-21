package com.oneliang.ktx.util.common

import com.oneliang.ktx.Constants

fun String.toIntSafely(defaultValue: Int = 0): Int = try {
    this.toInt()
} catch (e: Exception) {
    defaultValue
}

fun String.toLongSafely(defaultValue: Long = 0): Long = try {
    this.toLong()
} catch (e: Exception) {
    defaultValue
}

fun String.toFloatSafely(defaultValue: Float = 0f): Float = try {
    this.toFloat()
} catch (e: Exception) {
    defaultValue
}

fun String.toDoubleSafely(defaultValue: Double = 0.0): Double = try {
    this.toDouble()
} catch (e: Exception) {
    defaultValue
}

fun String.toBooleanSafely(defaultValue: Boolean = false): Boolean {
    return when {
        this.isBlank() -> defaultValue
        this.equals(true.toString(), true) -> true
        this.equals(false.toString(), true) -> false
        else -> defaultValue
    }
}

fun String.hexStringToByteArray(): ByteArray = ByteArray(this.length / 2) { this.substring(it * 2, it * 2 + 2).toInt(16).toByte() }

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

/**
 *
 * Method:only for regex,parse regex group when regex include group
 * @param regex
 * @return List<String>
</String> */
fun CharSequence.parseRegexGroup(regex: String): List<String> {
    val groupList = mutableListOf<String>()
    val matcher = regex.toRegex()
    var matchResult = matcher.find(this, 0)
    while (matchResult != null) {
        val groups = matchResult.groups
        val groupCount = groups.size
        for (index in 1 until groupCount) {
            val group = groups[index]
            if (group != null) {
                groupList.add(group.value)
            }
        }
        matchResult = matchResult.next()
    }
    return groupList
}

fun String?.nullToBlank(): String {
    return this ?: Constants.String.BLANK
}

object UnicodeRegex {
    const val REGEX_ALL = "\\\\u([A-Za-z0-9]*)"
    const val REGEX_CHINESE = "\\\\u([A-Za-z0-9]{4})"
    const val REGEX_ENGLISH_AND_NUMBER = "\\\\u([A-Za-z0-9]{2})"
    const val REGEX_SPECIAL = "\\\\u([A-Za-z0-9]{1})"
    internal const val FIRST_REGEX = "\\\\u"
}

fun String.toUnicode(): String {
    val stringBuilder = StringBuilder()
    val charArray = this
    for (char in charArray) {
        stringBuilder.append("\\u" + char.toInt().toString(radix = 16).toUpperCase())
    }
    return stringBuilder.toString()
}

fun String.fromUnicode(regex: String = UnicodeRegex.REGEX_ALL): String {
    val groupList = this.parseRegexGroup(regex)
    var result: String = this
    for (group in groupList) {
        result = result.replaceFirst(regex.toRegex(), group.toInt(radix = 16).toChar().toString())
    }
    return result
}