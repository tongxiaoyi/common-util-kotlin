package com.oneliang.ktx.util.json

import com.oneliang.ktx.Constants
import com.oneliang.ktx.util.common.KotlinClassUtil
import kotlin.reflect.KClass

fun Array<*>.toJson(jsonProcessor: JsonUtil.JsonProcessor = JsonUtil.DEFAULT_JSON_PROCESSOR) = joinToString(prefix = Constants.Symbol.MIDDLE_BRACKET_LEFT, postfix = Constants.Symbol.MIDDLE_BRACKET_RIGHT, separator = Constants.Symbol.COMMA) {
    if (it != null) {
        jsonProcessor.process<Any>(null, Constants.String.BLANK, it, false)
    } else {
        it.toString()
    }
}

fun BooleanArray.toJson() = joinToString(prefix = Constants.Symbol.MIDDLE_BRACKET_LEFT, postfix = Constants.Symbol.MIDDLE_BRACKET_RIGHT, separator = Constants.Symbol.COMMA) {
    it.toString()
}

fun ByteArray.toJson() = joinToString(prefix = Constants.Symbol.MIDDLE_BRACKET_LEFT, postfix = Constants.Symbol.MIDDLE_BRACKET_RIGHT, separator = Constants.Symbol.COMMA) {
    it.toString()
}

fun CharArray.toJson() = joinToString(prefix = Constants.Symbol.MIDDLE_BRACKET_LEFT, postfix = Constants.Symbol.MIDDLE_BRACKET_RIGHT, separator = Constants.Symbol.COMMA) {
    it.toString()
}

fun ShortArray.toJson() = joinToString(prefix = Constants.Symbol.MIDDLE_BRACKET_LEFT, postfix = Constants.Symbol.MIDDLE_BRACKET_RIGHT, separator = Constants.Symbol.COMMA) {
    it.toString()
}

fun IntArray.toJson() = joinToString(prefix = Constants.Symbol.MIDDLE_BRACKET_LEFT, postfix = Constants.Symbol.MIDDLE_BRACKET_RIGHT, separator = Constants.Symbol.COMMA) {
    it.toString()
}

fun LongArray.toJson() = joinToString(prefix = Constants.Symbol.MIDDLE_BRACKET_LEFT, postfix = Constants.Symbol.MIDDLE_BRACKET_RIGHT, separator = Constants.Symbol.COMMA) {
    it.toString()
}

fun FloatArray.toJson() = joinToString(prefix = Constants.Symbol.MIDDLE_BRACKET_LEFT, postfix = Constants.Symbol.MIDDLE_BRACKET_RIGHT, separator = Constants.Symbol.COMMA) {
    it.toString()
}

fun DoubleArray.toJson() = joinToString(prefix = Constants.Symbol.MIDDLE_BRACKET_LEFT, postfix = Constants.Symbol.MIDDLE_BRACKET_RIGHT, separator = Constants.Symbol.COMMA) {
    it.toString()
}

fun <T : Any> Iterable<T>.toJson(jsonProcessor: JsonUtil.JsonProcessor = JsonUtil.DEFAULT_JSON_PROCESSOR) = JsonUtil.iterableToJson(this, jsonProcessor)

fun <K : Any, V : Any> Map<K, V>.toJson(jsonProcessor: JsonUtil.JsonProcessor = JsonUtil.DEFAULT_JSON_PROCESSOR) = JsonUtil.mapToJson(this, jsonProcessor)

fun String.jsonToArrayBoolean(): Array<Boolean> = JsonUtil.jsonToArrayBoolean(this)

fun String.jsonToArrayInt(): Array<Int> = JsonUtil.jsonToArrayInt(this)

fun String.jsonToArrayLong(): Array<Long> = JsonUtil.jsonToArrayLong(this)

fun String.jsonToArrayDouble(): Array<Double> = JsonUtil.jsonToArrayDouble(this)

fun String.jsonToArrayString(): Array<String> = JsonUtil.jsonToArrayString(this)

fun <T : Any> String.jsonToObject(kClass: KClass<T>, classProcessor: KotlinClassUtil.KotlinClassProcessor = JsonUtil.DEFAULT_JSON_KOTLIN_CLASS_PROCESSOR): T = JsonUtil.jsonToObject(this, kClass, classProcessor)

fun <T : Any> String.jsonToObjectList(kClass: KClass<T>, classProcessor: KotlinClassUtil.KotlinClassProcessor = JsonUtil.DEFAULT_JSON_KOTLIN_CLASS_PROCESSOR): List<T> = JsonUtil.jsonToObjectList(this, kClass, classProcessor)