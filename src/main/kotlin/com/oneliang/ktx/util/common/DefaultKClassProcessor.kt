package com.oneliang.ktx.util.common

import com.oneliang.ktx.Constants
import kotlin.reflect.KClass

class DefaultKClassProcessor : KClassUtil.KClassProcessor {
    companion object {

        //	private static final String ZERO=String.valueOf(0);
        //	private static final String FALSE=String.valueOf(false);
        private const val DATE_LENGTH = Constants.Time.YEAR_MONTH_DAY.length
        private const val DATE_TIME_LENGTH = Constants.Time.YEAR_MONTH_DAY_HOUR_MINUTE_SECOND.length
    }

    /**
     * simple class type process
     * @param clazz
     * @param values
     * @param fieldName is null if not exist
     * @return Object
     */
    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> changeClassProcess(clazz: KClass<*>, values: Array<String>, fieldName: String): T? {
        var value: Any? = null
        val classType = KClassUtil.getClassType(clazz)
        when (classType) {
            KClassUtil.KClassType.KOTLIN_CHARACTER -> if (values.isNotEmpty() && values[0].isNotBlank()) {
                value = Character.valueOf(values[0].toCharArray()[0])
            }
            KClassUtil.KClassType.KOTLIN_STRING -> if (values.isNotEmpty()) {
                value = values[0]
            }
            KClassUtil.KClassType.KOTLIN_BYTE -> if (values.isNotEmpty() && values[0].isNotBlank()) {
                value = java.lang.Byte.valueOf(values[0])
            }
            KClassUtil.KClassType.KOTLIN_SHORT -> if (values.isNotEmpty() && values[0].isNotBlank()) {
                value = java.lang.Short.valueOf(values[0])
            }
            KClassUtil.KClassType.KOTLIN_INTEGER -> if (values.isNotEmpty() && values[0].isNotBlank()) {
                value = Integer.valueOf(values[0])
            }
            KClassUtil.KClassType.KOTLIN_LONG -> if (values.isNotEmpty() && values[0].isNotBlank()) {
                value = java.lang.Long.valueOf(values[0])
            }
            KClassUtil.KClassType.KOTLIN_FLOAT -> if (values.isNotEmpty() && values[0].isNotBlank()) {
                value = java.lang.Float.valueOf(values[0])
            }
            KClassUtil.KClassType.KOTLIN_DOUBLE -> if (values.isNotEmpty() && values[0].isNotBlank()) {
                value = java.lang.Double.valueOf(values[0])
            }
            KClassUtil.KClassType.KOTLIN_BOOLEAN -> if (values.isNotEmpty() && values[0].isNotBlank()) {
                value = java.lang.Boolean.valueOf(values[0])
            }
            KClassUtil.KClassType.JAVA_UTIL_DATE -> if (values.isEmpty() || values[0].isBlank()) {
                value = null
            } else {
                val valueLength = values[0].length
                if (valueLength == DATE_LENGTH) {
                    value = values[0].toUtilDate(Constants.Time.YEAR_MONTH_DAY)
                } else if (valueLength == DATE_TIME_LENGTH) {
                    value = values[0].toUtilDate()
                } else {
                    value = null
                }
            }
            KClassUtil.KClassType.KOTLIN_BYTE_ARRAY -> {
                val byteArray = Array(values.size) { index ->
                    if (values[index].isNotBlank()) {
                        values[index].toByte()
                    } else {
                        null
                    }
                }
                value = byteArray
            }
            KClassUtil.KClassType.KOTLIN_CHAR_ARRAY -> {
                val characterArray = Array(values.size) { i ->
                    if (values[i].isNotBlank()) {
                        values[i].toCharArray()[0]
                    } else {
                        null
                    }
                }
                value = characterArray
            }
            KClassUtil.KClassType.KOTLIN_STRING_ARRAY -> value = values
            KClassUtil.KClassType.KOTLIN_SHORT_ARRAY -> {
                val shortArray = Array(values.size) { i ->
                    if (values[i].isNotBlank()) {
                        values[i].toShort()
                    } else {
                        null
                    }
                }
                value = shortArray
            }
            KClassUtil.KClassType.KOTLIN_INT_ARRAY -> {
                val integerArray = Array(values.size) { i ->
                    if (values[i].isNotBlank()) {
                        values[i].toInt()
                    } else {
                        null
                    }
                }
                value = integerArray
            }
            KClassUtil.KClassType.KOTLIN_LONG_ARRAY -> {
                val longArray = Array(values.size) { i ->
                    if (values[i].isNotBlank()) {
                        values[i].toLong()
                    } else {
                        null
                    }
                }
                value = longArray
            }
            KClassUtil.KClassType.KOTLIN_FLOAT_ARRAY -> {
                val floatArray = Array(values.size) { i ->
                    if (values[i].isNotBlank()) {
                        values[i].toFloat()
                    } else {
                        null
                    }
                }
                value = floatArray
            }
            KClassUtil.KClassType.KOTLIN_DOUBLE_ARRAY -> {
                val doubleArray = Array(values.size) { i ->
                    if (values[i].isNotBlank()) {
                        values[i].toDouble()
                    } else {
                        null
                    }
                }
                value = doubleArray
            }
            KClassUtil.KClassType.KOTLIN_BOOLEAN_ARRAY -> {
                val booleanArray = Array(values.size) { i ->
                    if (values[i].isNotBlank()) {
                        values[i].toBoolean()
                    } else {
                        null
                    }
                }
                value = booleanArray
            }
            KClassUtil.KClassType.BYTE_ARRAY -> {
                val simpleByteArray = ByteArray(values.size) { i ->
                    if (values[i].isNotBlank()) {
                        values[i].toByte()
                    } else {
                        0
                    }
                }
                value = simpleByteArray
            }
            KClassUtil.KClassType.CHAR_ARRAY -> {
                val simpleCharArray = CharArray(values.size) { i ->
                    if (values[i].isNotBlank()) {
                        values[i].toCharArray()[0]
                    } else {
                        0.toChar()
                    }
                }
                value = simpleCharArray
            }
            KClassUtil.KClassType.SHORT_ARRAY -> {
                val simpleShortArray = ShortArray(values.size) { i ->
                    if (values[i].isNotBlank()) {
                        values[i].toShort()
                    } else {
                        0
                    }
                }
                value = simpleShortArray
            }
            KClassUtil.KClassType.INT_ARRAY -> {
                val simpleIntArray = IntArray(values.size) { i ->
                    if (values[i].isNotBlank()) {
                        values[i].toInt()
                    } else {
                        0
                    }
                }
                value = simpleIntArray
            }
            KClassUtil.KClassType.LONG_ARRAY -> {
                val simpleLongArray = LongArray(values.size) { i ->
                    if (values[i].isNotBlank()) {
                        values[i].toLong()
                    } else {
                        0L
                    }
                }
                value = simpleLongArray
            }
            KClassUtil.KClassType.FLOAT_ARRAY -> {
                val simpleFloatArray = FloatArray(values.size) { i ->
                    if (values[i].isNotBlank()) {
                        values[i].toFloat()
                    } else {
                        0.0F
                    }
                }
                value = simpleFloatArray
            }
            KClassUtil.KClassType.DOUBLE_ARRAY -> {
                val simpleDoubleArray = DoubleArray(values.size) { i ->
                    if (values[i].isNotBlank()) {
                        values[i].toDouble()
                    } else {
                        0.0
                    }
                }
                value = simpleDoubleArray
            }
            KClassUtil.KClassType.BOOLEAN_ARRAY -> {
                val simpleBooleanArray = BooleanArray(values.size) { i ->
                    if (values[i].isNotBlank()) {
                        values[i].toBoolean()
                    } else {
                        false
                    }
                }
                value = simpleBooleanArray
            }
            else -> {
                value = values
            }
        }
        return value as T
    }
}