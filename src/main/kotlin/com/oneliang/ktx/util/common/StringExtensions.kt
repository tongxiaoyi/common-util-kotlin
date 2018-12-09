package com.oneliang.ktx.util.common

import com.oneliang.ktx.Constants
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

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

fun String.toDoubleSafely(defaultValue: Double = 0.0): Double = try {
    this.toDouble()
} catch (e: Exception) {
    defaultValue
}

fun String.toBooleanSafely(defaultValue: Boolean = false): Boolean {
    return if (this.isBlank()) {
        defaultValue
    } else if (this.equals(true.toString(), true)) {
        true
    } else if (this.equals(false.toString(), true)) {
        false
    } else {
        defaultValue
    }
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

/**
 *
 * Method:only for regex,parse regex group when regex include group
 * @param string
 * @param regex
 * @return List<String>
</String> */
fun CharSequence.parseRegexGroup(regex: String): List<String> {
    val groupList = mutableListOf<String>()
    val pattern = Pattern.compile(regex)
    val matcher = pattern.matcher(this)
    val groupCount = matcher.groupCount()
    var count = 1
    if (matcher.find()) {
        while (count <= groupCount) {
            groupList.add(matcher.group(count))
            count++
        }
    }
    return groupList
}

/**
 *
 *
 * Method: check the string match the regex or not and return the match
 * field value
 * like {xxxx} can find xxxx
 *
 * @param string
 * @param regex
 * @param firstRegex
 * @param firstRegexReplace
 * @param lastRegexStringLength like {xxxx},last regex string is "}" so last regex string length equals 1
 * @return List<String>
</String> */
fun CharSequence.parseStringGroup(regex: String, firstRegex: String = Constants.String.BLANK, firstRegexReplace: String = Constants.String.BLANK, lastRegexStringLength: Int = 0): List<String> {
    val list = mutableListOf<String>()
    val lastRegexLength = if (lastRegexStringLength < 0) 0 else lastRegexStringLength
    val pattern = Pattern.compile(regex)
    val matcher = pattern.matcher(this)
    var group: String?
    var start = 0
    while (matcher.find(start)) {
        start = matcher.end()
        group = matcher.group()
        group = group!!.replaceFirst(firstRegex.toRegex(), firstRegexReplace)
        group = group.substring(0, group.length - lastRegexLength)
        list.add(group)
    }
    return list
}

fun String?.nullToBlank(): String {
    return this ?: Constants.String.BLANK
}

object UnicodeRegex {
    const val REGEX_ALL = "\\\\u[A-Za-z0-9]*"
    const val REGEX_CHINESE = "\\\\u[A-Za-z0-9]{4}"
    const val REGEX_ENGLISH_AND_NUMBER = "\\\\u[A-Za-z0-9]{2}"
    const val REGEX_SPECIAL = "\\\\u[A-Za-z0-9]{1}"
    internal const val FIRST_REGEX = "\\\\u"
}

fun String.toUnicode(): String {
    val stringBuilder = StringBuilder()
    val charArray = this.toCharArray()
    for (char in charArray) {
        stringBuilder.append("\\u" + Integer.toHexString(char.toInt()).toUpperCase())
    }
    return stringBuilder.toString()
}

fun String.fromUnicode(regex: String = UnicodeRegex.REGEX_ALL): String {
    val groupList = this.parseStringGroup(regex, com.oneliang.ktx.util.common.UnicodeRegex.FIRST_REGEX, Constants.String.BLANK, 0)
    var tempResult: String = this
    for (group in groupList) {
        tempResult = tempResult.replaceFirst(regex.toRegex(), Integer.parseInt(group, 16).toChar().toString())
    }
    return tempResult
}

fun String.MD5(): String {
    return Generator.MD5ByteArray(this.toByteArray(Charsets.UTF_8)).toHexString()
}