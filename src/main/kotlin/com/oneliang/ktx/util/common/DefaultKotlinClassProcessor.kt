package com.oneliang.ktx.util.common

import com.oneliang.ktx.Constants
import kotlin.reflect.KClass

class DefaultKotlinClassProcessor : KotlinClassUtil.KotlinClassProcessor {
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
    override fun <T : Any> changeClassProcess(clazz: KClass<T>, values: Array<String>, fieldName: String): T? {
        var value: Any? = null
        val classType = KotlinClassUtil.getClassType(clazz)
        when (classType) {
            KotlinClassUtil.ClassType.KOTLIN_CHARACTER -> if (values.isNotEmpty() && values[0].isNotBlank()) {
                value = Character.valueOf(values[0].toCharArray()[0])
            }
            KotlinClassUtil.ClassType.KOTLIN_STRING -> if (values.isNotEmpty()) {
                value = values[0]
            }
            KotlinClassUtil.ClassType.KOTLIN_BYTE -> if (values.isNotEmpty() && values[0].isNotBlank()) {
                value = java.lang.Byte.valueOf(values[0])
            }
            KotlinClassUtil.ClassType.KOTLIN_SHORT -> if (values.isNotEmpty() && values[0].isNotBlank()) {
                value = java.lang.Short.valueOf(values[0])
            }
            KotlinClassUtil.ClassType.KOTLIN_INTEGER -> if (values.isNotEmpty() && values[0].isNotBlank()) {
                value = Integer.valueOf(values[0])
            }
            KotlinClassUtil.ClassType.KOTLIN_LONG -> if (values.isNotEmpty() && values[0].isNotBlank()) {
                value = java.lang.Long.valueOf(values[0])
            }
            KotlinClassUtil.ClassType.KOTLIN_FLOAT -> if (values.isNotEmpty() && values[0].isNotBlank()) {
                value = java.lang.Float.valueOf(values[0])
            }
            KotlinClassUtil.ClassType.KOTLIN_DOUBLE -> if (values.isNotEmpty() && values[0].isNotBlank()) {
                value = java.lang.Double.valueOf(values[0])
            }
            KotlinClassUtil.ClassType.KOTLIN_BOOLEAN -> if (values.isNotEmpty() && values[0].isNotBlank()) {
                value = java.lang.Boolean.valueOf(values[0])
            }
            KotlinClassUtil.ClassType.JAVA_UTIL_DATE -> if (values.isEmpty() || values[0].isBlank()) {
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
            KotlinClassUtil.ClassType.KOTLIN_BYTE_ARRAY -> {
                val byteArray = Array(values.size) { index ->
                    if (values[index].isNotBlank()) {
                        values[index].toByte()
                    } else {
                        null
                    }
                }
                value = byteArray
            }
            KotlinClassUtil.ClassType.KOTLIN_CHAR_ARRAY -> {
                val characterArray = Array(values.size) { i ->
                    if (values[i].isNotBlank()) {
                        values[i].toCharArray()[0]
                    } else {
                        null
                    }
                }
                value = characterArray
            }
            KotlinClassUtil.ClassType.KOTLIN_STRING_ARRAY -> value = values
            KotlinClassUtil.ClassType.KOTLIN_SHORT_ARRAY -> {
                val shortArray = Array(values.size) { i ->
                    if (values[i].isNotBlank()) {
                        values[i].toShort()
                    } else {
                        null
                    }
                }
                value = shortArray
            }
            KotlinClassUtil.ClassType.KOTLIN_INT_ARRAY -> {
                val integerArray = Array(values.size) { i ->
                    if (values[i].isNotBlank()) {
                        values[i].toInt()
                    } else {
                        null
                    }
                }
                value = integerArray
            }
            KotlinClassUtil.ClassType.KOTLIN_LONG_ARRAY -> {
                val longArray = Array(values.size) { i ->
                    if (values[i].isNotBlank()) {
                        values[i].toLong()
                    } else {
                        null
                    }
                }
                value = longArray
            }
            KotlinClassUtil.ClassType.KOTLIN_FLOAT_ARRAY -> {
                val floatArray = Array(values.size) { i ->
                    if (values[i].isNotBlank()) {
                        values[i].toFloat()
                    } else {
                        null
                    }
                }
                value = floatArray
            }
            KotlinClassUtil.ClassType.KOTLIN_DOUBLE_ARRAY -> {
                val doubleArray = Array(values.size) { i ->
                    if (values[i].isNotBlank()) {
                        values[i].toDouble()
                    } else {
                        null
                    }
                }
                value = doubleArray
            }
            KotlinClassUtil.ClassType.KOTLIN_BOOLEAN_ARRAY -> {
                val booleanArray = Array(values.size) { i ->
                    if (values[i].isNotBlank()) {
                        values[i].toBoolean()
                    } else {
                        null
                    }
                }
                value = booleanArray
            }
            KotlinClassUtil.ClassType.BYTE_ARRAY -> {
                val simpleByteArray = ByteArray(values.size) { i ->
                    if (values[i].isNotBlank()) {
                        values[i].toByte()
                    } else {
                        0
                    }
                }
                value = simpleByteArray
            }
            KotlinClassUtil.ClassType.CHAR_ARRAY -> {
                val simpleCharArray = CharArray(values.size) { i ->
                    if (values[i].isNotBlank()) {
                        values[i].toCharArray()[0]
                    } else {
                        0.toChar()
                    }
                }
                value = simpleCharArray
            }
            KotlinClassUtil.ClassType.SHORT_ARRAY -> {
                val simpleShortArray = ShortArray(values.size) { i ->
                    if (values[i].isNotBlank()) {
                        values[i].toShort()
                    } else {
                        0
                    }
                }
                value = simpleShortArray
            }
            KotlinClassUtil.ClassType.INT_ARRAY -> {
                val simpleIntArray = IntArray(values.size) { i ->
                    if (values[i].isNotBlank()) {
                        values[i].toInt()
                    } else {
                        0
                    }
                }
                value = simpleIntArray
            }
            KotlinClassUtil.ClassType.LONG_ARRAY -> {
                val simpleLongArray = LongArray(values.size) { i ->
                    if (values[i].isNotBlank()) {
                        values[i].toLong()
                    } else {
                        0L
                    }
                }
                value = simpleLongArray
            }
            KotlinClassUtil.ClassType.FLOAT_ARRAY -> {
                val simpleFloatArray = FloatArray(values.size) { i ->
                    if (values[i].isNotBlank()) {
                        values[i].toFloat()
                    } else {
                        0.0F
                    }
                }
                value = simpleFloatArray
            }
            KotlinClassUtil.ClassType.DOUBLE_ARRAY -> {
                val simpleDoubleArray = DoubleArray(values.size) { i ->
                    if (values[i].isNotBlank()) {
                        values[i].toDouble()
                    } else {
                        0.0
                    }
                }
                value = simpleDoubleArray
            }
            KotlinClassUtil.ClassType.BOOLEAN_ARRAY -> {
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