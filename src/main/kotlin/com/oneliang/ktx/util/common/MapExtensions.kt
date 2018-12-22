package com.oneliang.ktx.util.common

import com.oneliang.ktx.Constants
import com.oneliang.ktx.exception.MethodInvokeException

fun <T : Any> Map<String, Array<String>>.toObject(instance: T, classProcessor: KotlinClassUtil.KotlinClassProcessor = KotlinClassUtil.DEFAULT_KOTLIN_CLASS_PROCESSOR) {
    val methods = instance.javaClass.methods
    if (this.isNotEmpty()) {
        for (method in methods) {
            val methodName = method.name
            val fieldName = if (methodName.startsWith(Constants.Method.PREFIX_SET)) {
                ObjectUtil.methodNameToFieldName(Constants.Method.PREFIX_SET, methodName)
            } else {
                Constants.String.BLANK
            }
            if (fieldName.isNotBlank()) {
                if (this.containsKey(fieldName)) {
                    val values = this[fieldName]!!
                    val classes = method.parameterTypes
                    if (classes.size == 1) {
                        val value = KotlinClassUtil.changeType(classes[0].kotlin, values, fieldName, classProcessor)
                        try {
                            method.invoke(instance, value)
                        } catch (e: Exception) {
                            throw MethodInvokeException(e)
                        }

                    }
                }
            }
        }
    }
}

@Throws(Exception::class)
fun <T : Any> Map<String, Array<String>>.toObjectList(clazz: Class<T>, classProcessor: KotlinClassUtil.KotlinClassProcessor = KotlinClassUtil.DEFAULT_KOTLIN_CLASS_PROCESSOR): List<T> {
    val methods = clazz.methods
    val list = mutableListOf<T>()
    if (this.isNotEmpty()) {
        for (method in methods) {
            val methodName = method.name
            val fieldName = if (methodName.startsWith(Constants.Method.PREFIX_SET)) {
                ObjectUtil.methodNameToFieldName(Constants.Method.PREFIX_SET, methodName)
            } else {
                Constants.String.BLANK
            }
            if (fieldName.isBlank()) {
                continue
            }
            if (this.containsKey(fieldName)) {
                val values = this[fieldName] ?: continue
                val classes = method.parameterTypes
                if (classes.size != 1) {
                    continue
                }
                for ((i, parameterValue) in values.withIndex()) {
                    val instance: T
                    if (i < list.size) {
                        instance = list[i]
                    } else {
                        try {
                            instance = clazz.newInstance()
                            list.add(instance)
                        } catch (e: Exception) {
                            throw e
                        }
                    }
                    val value = KotlinClassUtil.changeType(classes[0].kotlin, arrayOf(parameterValue), fieldName, classProcessor)
                    try {
                        method.invoke(instance, value)
                    } catch (e: Exception) {
                        throw MethodInvokeException(e)
                    }
                }
            }
        }
    }
    return list
}

fun <K, V, R> Map<K, V>.toList(transform: (K, V) -> R): List<R> {
    val list = mutableListOf<R>()
    this.forEach { (key, value) ->
        list.add(transform(key, value))
    }
    return list
}