package com.oneliang.ktx.util.json

import com.oneliang.ktx.Constants
import com.oneliang.ktx.util.common.KotlinClassUtil
import com.oneliang.ktx.util.common.toFormatString
import com.oneliang.ktx.util.common.transformQuotes
import java.util.*
import kotlin.reflect.KClass

open class DefaultJsonProcessor : JsonUtil.JsonProcessor {

    /**
     * default json processor
     */
    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> process(kClass: KClass<T>?, fieldName: String, value: Any?, ignoreFirstLetterCase: Boolean): String {
        if (value == null) {
            return Constants.String.NULL
        }
        val result: String
        val valueKClazz = value.javaClass.kotlin
        if (valueKClazz.java.isArray) {
            result = when {
                KotlinClassUtil.isBaseArray(valueKClazz) -> JsonUtil.baseArrayToJson(value)
                KotlinClassUtil.isSimpleArray(valueKClazz) -> JsonUtil.simpleArrayToJson(value as Array<Any>, this)
                else -> JsonUtil.objectArrayToJson(value as Array<Any>, emptyArray(), this, ignoreFirstLetterCase)
            }
        } else if (valueKClazz == Boolean::class
                || valueKClazz == Short::class
                || valueKClazz == Int::class
                || valueKClazz == Long::class
                || valueKClazz == Float::class
                || valueKClazz == Double::class
                || valueKClazz == Byte::class) {
            result = value.toString()
        } else if (valueKClazz == String::class || valueKClazz == Char::class || valueKClazz == CharSequence::class) {
            result = Constants.Symbol.DOUBLE_QUOTES + value.toString().transformQuotes() + Constants.Symbol.DOUBLE_QUOTES
        } else if (valueKClazz == Date::class) {
            result = Constants.Symbol.DOUBLE_QUOTES + (value as Date).toFormatString() + Constants.Symbol.DOUBLE_QUOTES
        } else {
            result = when (value) {
                is Iterable<*> -> JsonUtil.iterableToJson(value as Iterable<Any>, this, ignoreFirstLetterCase)
                is Map<*, *> -> JsonUtil.mapToJson(value as Map<Any, Any>, this, ignoreFirstLetterCase)
                else -> JsonUtil.objectToJson(value, emptyArray(), this, ignoreFirstLetterCase)
            }
        }
        return result
    }
}
