package com.oneliang.ktx.util.common

import com.oneliang.ktx.Constants
import java.util.*

/**
 * @author Dandelion
 * @since 2008-09-26
 */
object RequestUtil {

    /**
     * parse parameter string
     * string like this:class=com.lwx.test.loader.Test&method=test(java.lang.String=a,java.lang.Integer=10)"
     * @param parameterString
     * @return Map<String></String>,String[]>
     */
    fun parseParameterString(parameterString: String): Map<String, Array<String>> {
        val stringArray = parameterString.split(Constants.Symbol.AND)//.dropLastWhile { it.isEmpty() }.toTypedArray()
        val map = mutableMapOf<String, MutableList<String>>()
        val parameterMap = HashMap<String, Array<String>>()
        for (string in stringArray) {
            val equalIndex = string.indexOf(Constants.Symbol.EQUAL)
            if (equalIndex > 0) {
                val key = string.substring(0, equalIndex)
                val value = string.substring(equalIndex + 1, string.length)
                if (map.containsKey(key)) {
                    map[key]!!.add(value)
                } else {
                    val valueList = ArrayList<String>()
                    valueList.add(value)
                    map[key] = valueList
                }
            }
        }
        map.forEach { (key, value) ->
            parameterMap[key] = value.toTypedArray()
        }
        return parameterMap
    }
}

fun Map<String, Array<String>>.toParameterString(): String {
    var stringBuilder = StringBuilder()
    this.forEach { (key, value) ->
        value.forEach { it ->
            stringBuilder.append(key)
            stringBuilder.append(Constants.Symbol.EQUAL)
            stringBuilder.append(it)
            stringBuilder.append(Constants.Symbol.AND)
        }
    }
    if (stringBuilder.isNotEmpty()) {
        stringBuilder = stringBuilder.delete(stringBuilder.length - 1, stringBuilder.length)
    }
    return stringBuilder.toString()
}