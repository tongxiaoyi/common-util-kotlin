package com.oneliang.ktx.util.json

import com.oneliang.ktx.Constants
import com.oneliang.ktx.exception.MethodInvokeException
import com.oneliang.ktx.util.common.KotlinClassUtil
import com.oneliang.ktx.util.common.ObjectUtil
import kotlin.reflect.KClass

object JsonUtil {

    val DEFAULT_JSON_PROCESSOR: JsonProcessor = DefaultJsonProcessor()
    private val DEFAULT_KOTLIN_CLASS_PROCESSOR = KotlinClassUtil.DEFAULT_KOTLIN_CLASS_PROCESSOR

    /**
     *
     *
     * Method:basic array to json
     *
     *
     * @param object
     * @return String
     */
    fun baseArrayToJson(instance: Any): String {
        return when (instance) {
            is Array<*> -> {
                instance.toJson()
            }
            is BooleanArray -> {
                instance.toJson()
            }
            is ByteArray -> {
                instance.toJson()
            }
            is ShortArray -> {
                instance.toJson()
            }
            is IntArray -> {
                instance.toJson()
            }
            is LongArray -> {
                instance.toJson()
            }
            is FloatArray -> {
                instance.toJson()
            }
            is DoubleArray -> {
                instance.toJson()
            }
            is CharArray -> {
                instance.toJson()
            }
            else -> {
                throw JsonUtilException("unsupport in this method")
            }
        }
    }

    /**
     *
     *
     * Method:simple array to json
     *
     *
     * @param <T>
     * @param array
     * @param jsonProcessor
     * @return String
    </T> */
    fun <T : Any> simpleArrayToJson(array: Array<T>, jsonProcessor: JsonProcessor = DEFAULT_JSON_PROCESSOR): String {
        return array.toJson(jsonProcessor)
    }

    /**
     *
     *
     * Method:object array to json
     *
     *
     * @param <T>
     * @param array
     * @param fields
     * @param jsonProcessor
     * @param ignoreFirstLetterCase
     * @return String
    </T> */
    fun <T : Any> objectArrayToJson(array: Array<T>, fields: Array<String> = emptyArray(), jsonProcessor: JsonProcessor = DEFAULT_JSON_PROCESSOR, ignoreFirstLetterCase: Boolean): String {
        val string = StringBuilder()
        string.append(Constants.Symbol.MIDDLE_BRACKET_LEFT)
        val length = array.size
        for (i in 0 until length) {
            val instance = array[i]
            string.append(objectToJson(instance, fields, jsonProcessor, ignoreFirstLetterCase))
            if (i < length - 1) {
                string.append(Constants.Symbol.COMMA)
            }
        }
        string.append(Constants.Symbol.MIDDLE_BRACKET_RIGHT)
        return string.toString()
    }

    /**
     *
     *
     * Method:object array to json array,key means json's properties,value means
     * object field
     *
     *
     * @param <T>
     * @param array
     * @param fieldMap
     * @param jsonProcessor
     * @param ignoreFirstLetterCase
     * @return String
    </T> */
    fun <T : Any> objectArrayToJson(array: Array<T>, fieldMap: Map<String, String> = mapOf(), jsonProcessor: JsonProcessor = DEFAULT_JSON_PROCESSOR, ignoreFirstLetterCase: Boolean = false): String? {
        val result: String
        val string = StringBuilder()
        string.append(Constants.Symbol.MIDDLE_BRACKET_LEFT)
        val length = array.size
        for (i in 0 until length) {
            val instance = array[i]
            string.append(objectToJson(instance, fieldMap, jsonProcessor, ignoreFirstLetterCase))
            if (i < length - 1) {
                string.append(Constants.Symbol.COMMA)
            }
        }
        string.append(Constants.Symbol.MIDDLE_BRACKET_RIGHT)
        result = string.toString()
        return result
    }

    /**
     *
     *
     * Method: iterable to json
     *
     *
     * @param <T>
     * @param iterable
     * @param fields
     * @param jsonProcessor
     * @param ignoreFirstLetterCase
     * @return String
    </T> */
    fun <T : Any> iterableToJson(iterable: Iterable<T>, fields: Array<String> = emptyArray(), jsonProcessor: JsonProcessor = DEFAULT_JSON_PROCESSOR, ignoreFirstLetterCase: Boolean = false): String {
        val string = StringBuilder()
        string.append(Constants.Symbol.MIDDLE_BRACKET_LEFT)
        val iterator = iterable.iterator()
        while (iterator.hasNext()) {
            val instance = iterator.next()
            string.append(objectToJson(instance, fields, jsonProcessor, ignoreFirstLetterCase))
            if (iterator.hasNext()) {
                string.append(Constants.Symbol.COMMA)
            }
        }
        string.append(Constants.Symbol.MIDDLE_BRACKET_RIGHT)
        return string.toString()
    }

    /**
     *
     *
     * Method:iterable to json with field map,key means json's properties,value
     * means object field
     *
     *
     * @param <T>
     * @param iterable
     * @param fieldMap
     * @param jsonProcessor
     * @param ignoreFirstLetterCase
     * @return json
    </T> */
    fun <T : Any> iterableToJson(iterable: Iterable<T>, fieldMap: Map<String, String>, jsonProcessor: JsonProcessor = DEFAULT_JSON_PROCESSOR, ignoreFirstLetterCase: Boolean = false): String {
        val string = StringBuilder()
        string.append(Constants.Symbol.MIDDLE_BRACKET_LEFT)
        val iterator = iterable.iterator()
        while (iterator.hasNext()) {
            val instance = iterator.next()
            string.append(objectToJson(instance, fieldMap, jsonProcessor, ignoreFirstLetterCase))
            if (iterator.hasNext()) {
                string.append(Constants.Symbol.COMMA)
            }
        }
        string.append(Constants.Symbol.MIDDLE_BRACKET_RIGHT)
        return string.toString()
    }

    /**
     *
     *
     * Method: object to json string
     *
     *
     * @param instance
     * @param fields
     * @param jsonProcessor
     * @param ignoreFirstLetterCase
     * @return json string
     */
    fun <T : Any> objectToJson(instance: T, fields: Array<String>, jsonProcessor: JsonProcessor = DEFAULT_JSON_PROCESSOR, ignoreFirstLetterCase: Boolean = false): String {
        val objectJson = StringBuilder()
        val clazz = instance.javaClass.kotlin
        objectJson.append(Constants.Symbol.BIG_BRACKET_LEFT)
        if (fields.isNotEmpty()) {
            val length = fields.size
            for (i in 0 until length) {
                val fieldName = fields[i]
                var methodReturnValue = ObjectUtil.getterOrIsMethodInvoke(instance, fieldName, ignoreFirstLetterCase)
                methodReturnValue = jsonProcessor.process(clazz, fieldName, methodReturnValue, ignoreFirstLetterCase)
                objectJson.append(Constants.Symbol.DOUBLE_QUOTES + fieldName + Constants.Symbol.DOUBLE_QUOTES + Constants.Symbol.COLON + methodReturnValue.toString())
                if (i < length - 1) {
                    objectJson.append(Constants.Symbol.COMMA)
                }
            }
        } else {
            val subString = StringBuilder()
            val methods = instance.javaClass.methods
            for (method in methods) {
                val methodName = method.name
                val fieldName = ObjectUtil.methodNameToFieldName(methodName, ignoreFirstLetterCase)
                if (fieldName.isNotBlank()) {
                    var value: Any?
                    try {
                        value = method.invoke(instance)
                    } catch (e: Exception) {
                        throw MethodInvokeException(e)
                    }
                    value = jsonProcessor.process(clazz, fieldName, value, ignoreFirstLetterCase)
                    subString.append(Constants.Symbol.DOUBLE_QUOTES + fieldName + Constants.Symbol.DOUBLE_QUOTES + Constants.Symbol.COLON + value.toString() + Constants.Symbol.COMMA)
                }
            }
            if (subString.isNotEmpty()) {
                subString.delete(subString.length - 1, subString.length)
                objectJson.append(subString.toString())
            }
        }
        objectJson.append(Constants.Symbol.BIG_BRACKET_RIGHT)
        return objectJson.toString()
    }

    /**
     *
     *
     * Method:object to json with field map,key means json's properties,value
     * means object field
     *
     *
     * @param <T>
     * @param object
     * @param fieldMap
     * @param jsonProcessor
     * @param ignoreFirstLetterCase
     * @return json
    </T> */
    fun <T : Any> objectToJson(instance: T, fieldMap: Map<String, String>, jsonProcessor: JsonProcessor = DEFAULT_JSON_PROCESSOR, ignoreFirstLetterCase: Boolean = false): String {
        val objectJson = StringBuilder()
        val clazz = instance.javaClass.kotlin
        val iterator = fieldMap.entries.iterator()
        objectJson.append(Constants.Symbol.BIG_BRACKET_LEFT)
        while (iterator.hasNext()) {
            val entry = iterator.next()
            val key = entry.key
            val fieldName = entry.value
            var methodReturnValue = ObjectUtil.getterOrIsMethodInvoke(instance, fieldName, ignoreFirstLetterCase)
            methodReturnValue = jsonProcessor.process(clazz, fieldName, methodReturnValue, ignoreFirstLetterCase)
            objectJson.append(key + Constants.Symbol.COLON + methodReturnValue.toString())
            if (iterator.hasNext()) {
                objectJson.append(Constants.Symbol.COMMA)
            }
        }
        objectJson.append(Constants.Symbol.BIG_BRACKET_RIGHT)
        return objectJson.toString()
    }

    /**
     * jsonObject to object
     *
     * @param jsonObject
     * @param clazz
     * @param classProcessor
     * @param ignoreFirstLetterCase
     * @return T
     */
    fun <T : Any> jsonObjectToObject(jsonObject: JsonObject, clazz: KClass<T>, classProcessor: KotlinClassUtil.KotlinClassProcessor = DEFAULT_KOTLIN_CLASS_PROCESSOR, ignoreFirstLetterCase: Boolean = false): T {
        val instance: T
        val methods = clazz.java.methods
        try {
            instance = clazz.java.newInstance()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
        for (method in methods) {
            val methodName = method.name
            val fieldName = if (methodName.startsWith(Constants.Method.PREFIX_SET)) {
                ObjectUtil.methodNameToFieldName(Constants.Method.PREFIX_SET, methodName, ignoreFirstLetterCase)
            } else {
                Constants.String.BLANK
            }
            if (fieldName.isNotBlank()) {
                val classes = method.parameterTypes
                var value: Any? = null
                if (classes.size == 1) {
                    val objectClass = classes[0].kotlin
                    if (jsonObject.has(fieldName)) {
                        try {
                            if (KotlinClassUtil.isSimpleClass(objectClass)) {
                                if (!jsonObject.isNull(fieldName)) {
                                    value = KotlinClassUtil.changeType(objectClass, arrayOf(jsonObject.get(fieldName).toString()), fieldName, classProcessor)
                                } else {
                                    value = KotlinClassUtil.changeType(objectClass, emptyArray(), fieldName, classProcessor)
                                }
                            } else if (KotlinClassUtil.isBaseArray(objectClass) || KotlinClassUtil.isSimpleArray(objectClass)) {
                                if (!jsonObject.isNull(fieldName)) {
                                    value = jsonArrayToArray(jsonObject.getJsonArray(fieldName), objectClass, fieldName, classProcessor)
                                }
                            } else {
                                if (!jsonObject.isNull(fieldName)) {
                                    value = KotlinClassUtil.changeType(objectClass, arrayOf(jsonObject.get(fieldName).toString()), fieldName, classProcessor)
                                }
                            }
                        } catch (e: Exception) {
                            if (KotlinClassUtil.isSimpleClass(objectClass)) {
                                value = KotlinClassUtil.changeType(objectClass, emptyArray(), fieldName, classProcessor)
                            } else {
                                value = null
                            }
                        }
                        try {
                            method.invoke(instance, value)
                        } catch (e: Exception) {
                            throw MethodInvokeException(clazz.simpleName + Constants.Symbol.DOT + fieldName, e)
                        }
                    }
                }
            }
        }
        return instance
    }

    /**
     * jsonArray to array,just include base array and simple array
     *
     * @param jsonArray
     * @param clazz
     * @param fieldName
     * @param classProcessor
     * @return Object
     */
    private fun jsonArrayToArray(jsonArray: JsonArray, clazz: KClass<*>, fieldName: String, classProcessor: KotlinClassUtil.KotlinClassProcessor): Any {
        val length = jsonArray.length()
        val values = Array(length) { Constants.String.BLANK }
        for (i in 0 until length) {
            values[i] = jsonArray.get(i).toString()
        }
        return KotlinClassUtil.changeType(clazz, values, fieldName, classProcessor) ?: Constants.String.BLANK
    }

    /**
     * jsonArray to list
     *
     * @param <T>
     * @param jsonArray
     * @param clazz
     * @param classProcessor
     * @param ignoreFirstLetterCase
     * @return List<T>
    </T></T> */
    fun <T : Any> jsonArrayToList(jsonArray: JsonArray, clazz: KClass<T>, classProcessor: KotlinClassUtil.KotlinClassProcessor = DEFAULT_KOTLIN_CLASS_PROCESSOR, ignoreFirstLetterCase: Boolean = false): List<T> {
        val length = jsonArray.length()
        val list = mutableListOf<T>()
        for (i in 0 until length) {
            val instance = jsonArray.get(i)
            if (instance is JsonObject) {
                list.add(jsonObjectToObject(instance, clazz, classProcessor, ignoreFirstLetterCase))
            }
        }
        return list
    }

    /**
     * json to object
     *
     * @param json
     * @param clazz
     * @param classProcessor
     * @param ignoreFirstLetterCase
     * @return T
     */
    fun <T : Any> jsonToObject(json: String, clazz: KClass<T>, classProcessor: KotlinClassUtil.KotlinClassProcessor = DEFAULT_KOTLIN_CLASS_PROCESSOR, ignoreFirstLetterCase: Boolean = false): T {
        val jsonObject = JsonObject(json)
        return jsonObjectToObject(jsonObject, clazz, classProcessor, ignoreFirstLetterCase)
    }

    /**
     * json to object list
     *
     * @param json
     * @param clazz
     * @param classProcessor
     * @param ignoreFirstLetterCase
     * @return List<T>
    </T> */
    fun <T : Any> jsonToObjectList(json: String, clazz: KClass<T>, classProcessor: KotlinClassUtil.KotlinClassProcessor = DEFAULT_KOTLIN_CLASS_PROCESSOR, ignoreFirstLetterCase: Boolean = false): List<T> {
        val jsonArray = JsonArray(json)
        return jsonArrayToList(jsonArray, clazz, classProcessor, ignoreFirstLetterCase)
    }

    class JsonUtilException(message: String) : RuntimeException(message)

    interface JsonProcessor {

        /**
         * process
         *
         * @param <T>
         * @param clazz
         * @param fieldName
         * @param value
         * @param ignoreFirstLetterCase
         * @return String
         */
        fun <T : Any> process(clazz: KClass<T>? = null, fieldName: String, value: Any? = null, ignoreFirstLetterCase: Boolean): String
    }
}
